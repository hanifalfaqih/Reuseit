package id.hanifalfaqih.reuseit.ui.home.course

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
import id.hanifalfaqih.reuseit.data.repository.impl.CourseRepositoryImpl
import id.hanifalfaqih.reuseit.databinding.ActivityListCourseBinding
import id.hanifalfaqih.reuseit.helper.GenericViewModelFactory
import id.hanifalfaqih.reuseit.network.ApiConfig
import id.hanifalfaqih.reuseit.ui.detailcontent.DetailContentActivity
import id.hanifalfaqih.reuseit.ui.detailcontent.DetailContentActivity.Companion.CONTENT_ID
import kotlinx.coroutines.launch

class ListCourseActivity : AppCompatActivity() {

    private var _binding: ActivityListCourseBinding? = null
    private val binding get() = _binding!!

    private lateinit var courseViewModel: CourseViewModel
    private lateinit var courseAdapter: CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListCourseBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val apiService = ApiConfig.getApiService()
        val courseRepository = CourseRepositoryImpl(apiService)
        val courseRepositoryFactory = GenericViewModelFactory {
            CourseViewModel(courseRepository)
        }
        courseViewModel = ViewModelProvider(this, courseRepositoryFactory)[CourseViewModel::class.java]

        courseViewModel.getAllCourseContent()

        courseAdapter = CourseAdapter { courseId ->
            intentToDetailContent(courseId)
        }

        observeData()
        initRecyclerView()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            courseViewModel.listAllCourseContent.observe(this@ListCourseActivity) { listAllCourse ->
                courseAdapter.submitList(listAllCourse)
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvAllListCourses.apply {
            layoutManager = LinearLayoutManager(context)
            binding.rvAllListCourses.adapter = courseAdapter
        }
    }

    private fun intentToDetailContent(id: Int) {
        val intent = Intent(this, DetailContentActivity::class.java)
        intent.putExtra(CONTENT_ID, id)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}