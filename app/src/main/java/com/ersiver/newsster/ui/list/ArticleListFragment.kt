package com.ersiver.newsster.ui.list

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.preference.PreferenceManager
import com.ersiver.newsster.R
import com.ersiver.newsster.databinding.ArticleListFragmentBinding
import com.ersiver.newsster.util.DEFAULT_LANGUAGE
import com.ersiver.newsster.util.PREF_LANGUAGE_KEY
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class ArticleListFragment : Fragment() {
    private val viewModel: ArticleListViewModel by hiltNavGraphViewModels(R.id.navgraph)
    private var job: Job? = null
    private lateinit var adapter: ArticleAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var toolbar: Toolbar
    private var _binding: ArticleListFragmentBinding? = null
    private val binding
        get() = _binding!!

    override
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArticleListFragmentBinding.inflate(inflater, container, false)

        setupCustomToolbar()
        getPrefs()
        initAdapter()
        getNewsAndNotifyAdapter()
        showCategoryPopupMenu()

        viewModel.categoryLocalLiveData.observe(viewLifecycleOwner) { category ->
            updateToolbarTitle(category)
        }

        viewModel.categoryLiveData.observe(viewLifecycleOwner) {
            getNewsAndNotifyAdapter()
        }

        viewModel.languageLiveData.observe(viewLifecycleOwner) {
            getNewsAndNotifyAdapter()
        }

        return binding.root
    }

    private fun setupCustomToolbar() {
        toolbar = binding.homeToolbar
    }

    private fun getPrefs() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val language = sharedPreferences.getString(PREF_LANGUAGE_KEY, DEFAULT_LANGUAGE).toString()
        viewModel.updateLanguage(language)
    }

    private fun initAdapter() {
        adapter = ArticleAdapter()
        binding.articleList.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            binding.articleList.isVisible = loadState.refresh is LoadState.NotLoading
            binding.progress.isVisible = loadState.refresh is LoadState.Loading
            manageErrors(loadState)
        }
    }

    private fun getNewsAndNotifyAdapter() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.loadNews().collectLatest { adapter.submitData(it) }
        }
    }

    //Title matches the category name in the localized language.
    private fun updateToolbarTitle(categoryLocalized: Int) {
        val titles = resources.getStringArray(R.array.categories_titles)
        toolbar.title = titles[categoryLocalized]
    }

    private fun manageErrors(loadState: CombinedLoadStates) {
        binding.errorText.isVisible = loadState.refresh is LoadState.Error
        binding.retryButton.isVisible = loadState.refresh is LoadState.Error
        binding.retryButton.setOnClickListener { adapter.retry() }

        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.append as? LoadState.Error
            ?: loadState.prepend as? LoadState.Error

        errorState?.let {
            val errorText = resources.getString(R.string.error) + it.error.toString()
            binding.errorText.text = errorText
        }
    }

    private fun showCategoryPopupMenu() {
        binding.apply {
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.settings) {
                    navigateToSettings()
                } else
                    displayCategoriesPicker()
                true
            }
        }
    }

    private fun navigateToSettings() {
        findNavController().navigate(
            ArticleListFragmentDirections
                .actionArticleListFragmentToSettingsFragment()
        )
    }

    private fun displayCategoriesPicker() {
        val items = resources.getStringArray(R.array.categories_titles)
        val itemsValue = resources.getStringArray(R.array.categories_values)

        MaterialAlertDialogBuilder(requireActivity(), R.style.NewssterMaterialAlertDialog)
            .setTitle(resources.getString(R.string.dialog_title))
            .setIcon(R.drawable.ic_logo)
            .setItems(items) { _, which ->
                val category: String = itemsValue[which]

                viewModel.updateCategory(category, which)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}