package com.ersiver.newsster.ui.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ersiver.newsster.R
import com.ersiver.newsster.databinding.ArticleFragmentBinding
import com.ersiver.newsster.model.Article
import com.ersiver.newsster.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : Fragment() {
    @VisibleForTesting
    private val articleViewModel: ArticleViewModel by hiltNavGraphViewModels(R.id.navgraph)
    private val args: ArticleFragmentArgs by navArgs()
    private var _binding: ArticleFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArticleFragmentBinding.inflate(inflater, container, false)

        setupToolbar()
        onContinueReadingClick()

        articleViewModel.getArticle(args.articleId).observe(viewLifecycleOwner) { article ->
            displayArticle(article)
        }

        articleViewModel.shareArticleEvent.observe(viewLifecycleOwner, EventObserver { article ->
            shareArticle(article)
        })

        articleViewModel.openWebsiteEvent.observe(viewLifecycleOwner, EventObserver { article ->
            openWebsite(article)
        })

        return binding.root
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setTitle(R.string.app_name)
            setupWithNavController()
            setupToolbarMenu()
        }
    }

    private fun Toolbar.setupToolbarMenu() {
        setOnMenuItemClickListener {
            if (it.itemId == R.id.share)

                //Inform articleViewModel the button was clicked.
                articleViewModel.shareArticle()
            true
        }
    }

    private fun Toolbar.setupWithNavController() {
        navigationContentDescription = resources.getString(R.string.nav_up)
        setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun onContinueReadingClick() {
        binding.article.continueReadingButton.setOnClickListener {
            //Inform articleViewModel the button was clicked.
            articleViewModel.openWebsite()
        }
    }

    //Open the article url in the browser.
    private fun openWebsite(article: Article) {
        val webPage: Uri = Uri.parse(article.url)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(requireContext().packageManager) != null)
            startActivity(intent)
    }

    //Bind all data to the layout's views.
    private fun displayArticle(article: Article) {
        binding.apply {
            this.article.author.loadOrGone(article.author)
            this.article.title.loadOrGone(article.title.formatTitle())
            this.article.content.loadOrGone(article.content.formatContent())
            this.article.description.loadOrGone(article.description)
            this.article.date.loadOrGone(article.date.formatDate())
            this.article.source.loadOrGone(article.source.name)
            Glide.with(requireContext()).load(article.imgUrl).into(articleImage)
        }
    }

    //Share article url
    private fun shareArticle(article: Article) {
        val mimeType = "text/plain"
        ShareCompat.IntentBuilder
            .from(requireActivity())
            .setType(mimeType)
            .setChooserTitle(resources.getString(R.string.share_article))
            .setText(article.url)
            .startChooser()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}