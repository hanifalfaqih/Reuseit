package id.hanifalfaqih.reuseit.ui.detailcontent

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import id.hanifalfaqih.reuseit.R
import id.hanifalfaqih.reuseit.data.model.Content
import id.hanifalfaqih.reuseit.data.repository.impl.DetailContentRepositoryImpl
import id.hanifalfaqih.reuseit.databinding.ActivityDetailContentBinding
import id.hanifalfaqih.reuseit.helper.GenericViewModelFactory
import id.hanifalfaqih.reuseit.network.ApiConfig
import kotlinx.coroutines.launch

class DetailContentActivity : AppCompatActivity() {

    private var _binding: ActivityDetailContentBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailContentViewModel: DetailContentViewModel
    private var content: Content? = null

    private var contentId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailContentBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val apiService = ApiConfig.getApiService()
        val detailContentRepository = DetailContentRepositoryImpl(apiService)
        val detailRepositoryFactory = GenericViewModelFactory {
            DetailContentViewModel(detailContentRepository)
        }
        detailContentViewModel =
            ViewModelProvider(this, detailRepositoryFactory)[DetailContentViewModel::class.java]

        contentId = intent.getIntExtra(CONTENT_ID, 0)
        detailContentViewModel.getDetailContent(contentId)

        observeData()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            detailContentViewModel.detailContent.observe(this@DetailContentActivity) { detailContent ->
                Log.d(DetailContentActivity::class.java.simpleName, detailContent.toString())
                setContentData(detailContent)
            }
        }
    }

    private fun setContentData(content: Content?) {
        content?.let {
            binding.toolbar.title = it.title

            Glide.with(this)
                .load(it.imageThumbnail)
                .into(binding.ivThumbnail)

            if (it.imageContent == null) {
                binding.ivContent.visibility = View.GONE
            } else {
                Glide.with(this)
                    .load(it.imageContent)
                    .into(binding.ivContent)
            }

            binding.tvTypeContent.text = it.typeContent
            binding.tvTypeTrash.text = it.typeTrash
            binding.tvViewsContent.text = it.views.toString()
            binding.tvHeader.text = it.header
            binding.tvContent.text = it.content
        }
    }

    companion object {
        const val CONTENT_ID = "CONTENT_ID"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}