package id.hanifalfaqih.reuseit.home.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.unity3d.player.UnityPlayerActivity
import id.hanifalfaqih.reuseit.data.repository.impl.CourseRepositoryImpl
import id.hanifalfaqih.reuseit.data.repository.impl.DIYRepositoryImpl
import id.hanifalfaqih.reuseit.databinding.FragmentHomeBinding
import id.hanifalfaqih.reuseit.helper.GenericViewModelFactory
import id.hanifalfaqih.reuseit.home.HomeActivity
import id.hanifalfaqih.reuseit.network.ApiConfig
import id.hanifalfaqih.reuseit.ui.detailcontent.DetailContentActivity
import id.hanifalfaqih.reuseit.ui.detailcontent.DetailContentActivity.Companion.CONTENT_ID
import id.hanifalfaqih.reuseit.ui.home.course.CourseAdapter
import id.hanifalfaqih.reuseit.ui.home.course.CourseViewModel
import id.hanifalfaqih.reuseit.ui.home.course.ListCourseActivity
import id.hanifalfaqih.reuseit.ui.home.diy.DIYAdapter
import id.hanifalfaqih.reuseit.ui.home.diy.DIYViewModel
import id.hanifalfaqih.reuseit.ui.home.diy.ListDIYActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    companion object {
        private const val UNITY_REQUEST_CODE = 1
    }

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var diyViewModel: DIYViewModel
    private lateinit var courseViewModel: CourseViewModel

    private lateinit var courseAdapter: CourseAdapter
    private lateinit var diyAdapter: DIYAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = ApiConfig.getApiService()
        val courseRepository = CourseRepositoryImpl(apiService)
        val diyRepository = DIYRepositoryImpl(apiService)

        val courseRepositoryFactory = GenericViewModelFactory {
            CourseViewModel(courseRepository)
        }
        val diyRepositoryFactory = GenericViewModelFactory {
            DIYViewModel(diyRepository)
        }
        courseViewModel = ViewModelProvider(this, courseRepositoryFactory)[CourseViewModel::class.java]
        diyViewModel = ViewModelProvider(this, diyRepositoryFactory)[DIYViewModel::class.java]

        courseViewModel.getTop5CourseContent()
        diyViewModel.getTop5DIYContent()

        courseAdapter = CourseAdapter { courseId ->
            intentToDetailContent(courseId)
        }

        diyAdapter = DIYAdapter { diyId ->
            intentToDetailContent(diyId)
        }

        binding.tvShowAllDiy.setOnClickListener {
            startActivity(Intent(requireContext(), ListDIYActivity::class.java))
//            Toast.makeText(requireContext(), "GO TO ALL LIST DIY", Toast.LENGTH_SHORT).show()
        }

        binding.tvShowAllCourses.setOnClickListener {
            startActivity(Intent(requireContext(), ListCourseActivity::class.java))
//            Toast.makeText(requireContext(), "GO TO ALL LIST COURSES", Toast.LENGTH_SHORT).show()
        }

        binding.gamesTrashHeroes.setOnClickListener {
            val intent = Intent(requireContext(), UnityPlayerActivity::class.java)
            startActivityForResult(intent, UNITY_REQUEST_CODE)
        }

        observeData()
        initRecyclerView()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            diyViewModel.listTop5DIYContent.observe(viewLifecycleOwner) { listTopDiy ->
                diyAdapter.submitList(listTopDiy)
            }

            courseViewModel.listTop5CourseContent.observe(viewLifecycleOwner) { listTopCourse ->
                courseAdapter.submitList(listTopCourse)
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvTopListDiy.apply {
            layoutManager = LinearLayoutManager(context)
            binding.rvTopListDiy.adapter = courseAdapter
        }

        binding.rvTopListCourses.apply {
            layoutManager = LinearLayoutManager(context)
            binding.rvTopListCourses.adapter = diyAdapter
        }
    }

    private fun intentToDetailContent(id: Int) {
        val intent = Intent(requireContext(), DetailContentActivity::class.java)
        intent.putExtra(CONTENT_ID, id)
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UNITY_REQUEST_CODE) {
            // Tangani logika ketika keluar dari Unity
            Log.i("Coba", "Unity activity selesai")
            // Anda bisa menambahkan logika lain seperti menampilkan pesan
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
        }
    }
}