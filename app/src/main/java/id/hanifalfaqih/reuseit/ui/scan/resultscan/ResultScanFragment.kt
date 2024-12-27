package id.hanifalfaqih.reuseit.ui.scan.resultscan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.hanifalfaqih.reuseit.databinding.FragmentResultScanDialogBinding
import id.hanifalfaqih.reuseit.ml.wasteProcessingInfo

// TODO: Customize parameter argument names
const val ARG_WASTE_TYPE = "TYPE_WASTE"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    ResultScanFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class ResultScanFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentResultScanDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentResultScanDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val wasteType = arguments?.getString(ARG_WASTE_TYPE)

        val detailInformation = wasteProcessingInfo[wasteType]
        if (detailInformation != null) {
            binding.tvTypeWaste.text = wasteType
            binding.tvWhatIsWaste.text = detailInformation[0]
            binding.tvHowProcessWaste.text = detailInformation[1]
        } else {
            binding.tvWhatIsWaste.text = "Informasi tidak tersedia"
            binding.tvHowProcessWaste.text = "Informasi tidak tersedia"
        }
    }


    companion object {

        // TODO: Customize parameters
        fun newInstance(typeWaste: String): ResultScanFragment =
            ResultScanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_WASTE_TYPE, typeWaste)
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}