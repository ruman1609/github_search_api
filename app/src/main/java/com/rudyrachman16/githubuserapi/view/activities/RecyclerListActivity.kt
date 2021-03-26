package com.rudyrachman16.githubuserapi.view.activities

import android.app.ActivityOptions
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.transition.Fade
import android.util.Pair
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudyrachman16.githubuserapi.R
import com.rudyrachman16.githubuserapi.data.models.SearchUser
import com.rudyrachman16.githubuserapi.databinding.ActivityRecyclerListBinding
import com.rudyrachman16.githubuserapi.view.ListViewModel
import com.rudyrachman16.githubuserapi.view.adapters.ListAdapter

class RecyclerListActivity : AppCompatActivity() {
    private var binding: ActivityRecyclerListBinding? = null
    private val bind get() = binding!!

    private var listAdaptered: ListAdapter? = null
    private val listAdapter get() = listAdaptered!!
    private val list = ArrayList<SearchUser>()

    private var listViewModeler: ListViewModel? = null
    private val listViewModel get() = listViewModeler!!

    companion object {
        @JvmStatic
        private var empty = true
        val transitions = arrayOf(
            "pictureTransition",
            "usernameTransition"
        )
        const val USER_KEY = "user"
        const val FOLLOWING_KEY = "following"
        const val FOLLOWERS_KEY = "followers"
        const val DETAIL_KEY = "detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerListBinding.inflate(layoutInflater)
        setContentView(bind.root)
        title = getString(R.string.github_search)
        setSupportActionBar(bind.toolbar.root)

        listViewModeler = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ListViewModel::class.java)
        listAdaptered = ListAdapter(this@RecyclerListActivity) { it1, bind ->
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@RecyclerListActivity,
                Pair.create(bind.imageView, transitions[0]),
                Pair.create(bind.username, transitions[1])
            )
            val move = Intent(this, DetailActivity::class.java).apply {
                putExtra(USER_KEY, it1)
            }
            bind.loading.visibility = View.GONE
            startActivity(move, options.toBundle())
        }
        bind.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RecyclerListActivity)
            setHasFixedSize(true)
            adapter = listAdapter
        }
        listAdapter.notifyDataSetChanged()

        if (empty) changeInformation(R.color.black)
        listViewModel.getList().observe(this@RecyclerListActivity) {  // Observe must at onCreate()
            if (!empty) {
                list.addAll(it)
                if (list.isEmpty()) changeInformation(R.color.red)
                else listAdapter.setList(list)
                bind.loading.visibility = View.GONE
            } else changeInformation(R.color.black)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        listAdaptered = null
        listViewModeler = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = (menu.findItem(R.id.search).actionView as SearchView).apply {
            queryHint = getString(R.string.search_hint)
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnCloseListener {
                clearList()
                true
            }
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                bind.information.visibility = View.GONE
                changeTextListener(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.locale)
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        return true
    }

    private fun changeInformation(color: Int) {
        bind.information.visibility = View.VISIBLE
        bind.information.text = if (color == R.color.black) getString(R.string.information)
        else getString(R.string.not_found)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            bind.information.setTextColor(
                resources.getColorStateList(
                    color,
                    null
                )
            )
        else
            bind.information.setTextColor(resources.getColor(color))
    }

    private fun changeTextListener(newText: String) {
        if (newText.isEmpty()) clearList()
        else {
            empty = false
            list.clear()
            listAdapter.setList(list)
            listViewModel.setList(this, newText)
            bind.loading.visibility = View.VISIBLE
        }
    }

    private fun clearList() {
        bind.loading.visibility = View.GONE
        changeInformation(R.color.black)
        list.clear()
        listAdapter.setList(list)
        empty = true
    }
}