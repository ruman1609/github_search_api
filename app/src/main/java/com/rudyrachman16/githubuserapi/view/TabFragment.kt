package com.rudyrachman16.githubuserapi.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rudyrachman16.githubuserapi.R
import com.rudyrachman16.githubuserapi.data.models.SearchUser
import com.rudyrachman16.githubuserapi.databinding.FragmentTabBinding
import com.rudyrachman16.githubuserapi.view.activities.DetailActivity
import com.rudyrachman16.githubuserapi.view.activities.RecyclerListActivity
import com.rudyrachman16.githubuserapi.view.adapters.ListAdapter
import java.util.*

class TabFragment : Fragment() {
    companion object {
        private const val KEY = "tabs_keys"
        private const val POSITION = "tabs_position"

        @JvmStatic
        fun newInstance(json: String, position: Int) = TabFragment().apply {
            arguments = Bundle().apply {
                putString(KEY, json)
                putInt(POSITION, position)
            }
        }
    }

    private var binding: FragmentTabBinding? = null
    private val bind get() = binding!!

    private var adaptered: ListAdapter? = null
    private val adapterList get() = adaptered!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val json = arguments?.getString(KEY, null)
        val position = arguments?.getInt(POSITION, -1)
        if (json != null) {
            val gson = Gson()
            val results: ArrayList<SearchUser> = gson.fromJson(
                json,
                object : TypeToken<ArrayList<SearchUser>>() {}.type
            )
            if (results.isEmpty()) {
                bind.information.text =
                    getString(R.string.no_list, getString(DetailActivity.tabTitle[position!!]))
                bind.information.visibility = View.VISIBLE
            }
            adaptered = ListAdapter(requireContext()) { it, bind ->
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    requireActivity(),
                    Pair.create(bind.imageView, RecyclerListActivity.transitions[0]),
                    Pair.create(bind.username, RecyclerListActivity.transitions[1])
                )
                val move = Intent(requireActivity(), DetailActivity::class.java).apply {
                    putExtra(RecyclerListActivity.USER_KEY, it)
                }
                bind.loading.visibility = View.GONE
                startActivity(move, options.toBundle())
            }
            adapterList.setList(results)
            bind.recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                adapter = adapterList
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}