package id.hanifalfaqih.reuseit.home.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.unity3d.player.UnityPlayerActivity
import id.hanifalfaqih.reuseit.databinding.FragmentNotificationsBinding
import id.hanifalfaqih.reuseit.home.HomeActivity

class NotificationsFragment : Fragment() {
    companion object {
        private const val UNITY_REQUEST_CODE = 1
    }

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGoToGame.setOnClickListener {
            val intent = Intent(requireContext(), UnityPlayerActivity::class.java)
            startActivityForResult(intent, UNITY_REQUEST_CODE)
        }
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