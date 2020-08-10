package com.ersiver.newsster.ui.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.ersiver.newsster.R
import com.ersiver.newsster.databinding.ArticleFragmentBinding
import com.ersiver.newsster.model.Article
import com.ersiver.newsster.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import com.ersiver.newsster.util.*

@AndroidEntryPoint
class ArticleFragment : Fragment() {
    private val articleViewModel by viewModels<ArticleViewModel>()

    private val args: ArticleFragmentArgs by navArgs()
    private val article: Article by lazy {
        args.selectedArticle
    }
    private var _binding: ArticleFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ArticleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupActionBarWithNavController()
        setupOpenWebsiteButton()

        articleViewModel.fetchArticle(article.id)

        articleViewModel.articleLiveData.observe(viewLifecycleOwner, Observer { article ->
            displayArticle(article)
        })

        articleViewModel.shareArticleEvent.observe(viewLifecycleOwner, EventObserver { articleUrl ->
            shareArticle(articleUrl)
        })

        articleViewModel.openWebsiteEvent.observe(viewLifecycleOwner, EventObserver { articleUrl ->
            openWebsite(articleUrl)
        })
    }

    private fun setupOpenWebsiteButton() {
        binding.article.openWebsite.setOnClickListener {
            articleViewModel.openWebsite(article.url)
        }
    }

    private fun displayArticle(selectedArticle: Article) {
        binding.apply {
            article.author.loadOrGone(selectedArticle.author)
            article.title.loadOrGone(selectedArticle.title.formatTitle())
            article.content.loadOrGone(selectedArticle.content.formatContent())
            article.description.loadOrGone(selectedArticle.description)
            article.date.loadOrGone(selectedArticle.date.formatDate())
            article.source.loadOrGone(selectedArticle.source.name)
            Glide.with(requireContext()).load(selectedArticle.imgUrl).into(articleImage)
        }
    }

    private fun setupActionBarWithNavController() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.apply {
            toolbar.setupWithNavController(navController, appBarConfiguration)
            toolbar.setTitle(R.string.app_name)
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.share)
                    articleViewModel.shareArticle(this@ArticleFragment.article.id)
                true
            }
        }
    }

    private fun shareArticle(articleUrl: String) {
        val mimeType = "text/plain"
        ShareCompat.IntentBuilder
            .from(requireActivity())
            .setType(mimeType)
            .setChooserTitle("Share link")
            .setText(articleUrl)
            .startChooser()
    }

    private fun openWebsite(articleUrl: String) {
        val webPage: Uri = Uri.parse(articleUrl)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(requireContext().packageManager) != null)
            startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}