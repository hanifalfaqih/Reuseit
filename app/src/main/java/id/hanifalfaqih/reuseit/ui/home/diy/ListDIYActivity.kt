package id.hanifalfaqih.reuseit.ui.home.diy

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.hanifalfaqih.reuseit.R
import id.hanifalfaqih.reuseit.data.repository.impl.DIYRepositoryImpl
import id.hanifalfaqih.reuseit.databinding.ActivityListDiyBinding
import id.hanifalfaqih.reuseit.helper.GenericViewModelFactory
import id.hanifalfaqih.reuseit.network.ApiConfig
import id.hanifalfaqih.reuseit.ui.detailcontent.DetailContentActivity
import id.hanifalfaqih.reuseit.ui.detailcontent.DetailContentActivity.Companion.CONTENT_ID
import kotlinx.coroutines.launch

class ListDIYActivity : AppCompatActivity() {

    private var _binding: ActivityListDiyBinding? = null
    private val binding get() = _binding!!

    private lateinit var diyViewModel: DIYViewModel
    private lateinit var diyAdapter: DIYAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListDiyBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val apiService = ApiConfig.getApiService()
        val diyRepository = DIYRepositoryImpl(apiService)
        val diyRepositoryFactory = GenericViewModelFactory {
            DIYViewModel(diyRepository)
        }
        diyViewModel = ViewModelProvider(this, diyRepositoryFactory)[DIYViewModel::class.java]

        diyViewModel.getAllDIYContent()

        diyAdapter = DIYAdapter { diyId ->
            intentToDetailContent(diyId)
        }

        observeData()
        initRecyclerView()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            diyViewModel.listAllDIYContent.observe(this@ListDIYActivity) { listAllDiy ->
                diyAdapter.submitList(listAllDiy)
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvAllListDiy.apply {
            layoutManager = LinearLayoutManager(context)
            binding.rvAllListDiy.adapter = diyAdapter
        }
    }

    private fun intentToDetailContent(id: Int) {
        val intent = Intent(this, DetailContentActivity::class.java)
        intent.putExtra(CONTENT_ID, id)
        startActivity(intent)
    }

}