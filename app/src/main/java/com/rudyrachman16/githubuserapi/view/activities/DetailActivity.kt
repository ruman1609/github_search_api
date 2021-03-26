package com.rudyrachman16.githubuserapi.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.transition.Fade
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.rudyrachman16.githubuserapi.R
import com.rudyrachman16.githubuserapi.data.models.User
import com.rudyrachman16.githubuserapi.databinding.ActivityDetailBinding
import com.rudyrachman16.githubuserapi.view.adapters.TabAdapter
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailActivity : AppCompatActivity() {
    companion object {
        @StringRes
        val tabTitle = intArrayOf(
            R.string.followers,
            R.string.following,
        )
    }

    private var followers: String? = null
    private var following: String? = null

    private var binding: ActivityDetailBinding? = null
    private val bind get() = binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)
        val user: User = intent.getParcelableExtra(RecyclerListActivity.USER_KEY)

        setSupportActionBar(bind.toolbar.root)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        val fade = Fade().apply {
            excludeTarget(bind.toolbar.root, true)
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
        }
        window.enterTransition = fade
        window.exitTransition = fade

        title = user.id.toString()
        bind.username.text = user.username
        Glide.with(this)
            .load(user.picUrl)
            .apply(RequestOptions().override(100))
            .into(bind.imageView)

        val numberFormat = NumberFormat.getNumberInstance()
        val decimalFormat = (numberFormat as DecimalFormat).apply {
            isGroupingUsed = true
            applyPattern("#,###.#")
        }
        user.getDetail(this) {
            bind.loadingCount.visibility = View.GONE
            bind.loadingDetail.visibility = View.GONE
            bind.followers.text = groupNum(it.followers, decimalFormat)
            bind.following.text = groupNum(it.following, decimalFormat)
            bind.repos.text = groupNum(it.repo, decimalFormat)
            additionalInformation(it.location, bind.location)
            additionalInformation(it.company, bind.company)
            additionalInformation(it.name, bind.name)
            bind.url.setOnClickListener { _ ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(it.url)
                    )
                )
            }
        }
        user.getFollowers(this) {
            followers = Gson().toJson(it)
            setTab(followers, following)
        }
        user.getFollowing(this) {
            following = Gson().toJson(it)
            setTab(followers, following)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu.findItem(R.id.search).isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.locale) {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTab(followers: String?, following: String?) {
        if (following == null || followers == null) return
        bind.loadingFoll.visibility = View.GONE
        val tabAdapter = TabAdapter(this, arrayOf(followers, following))
        bind.viewPager2.adapter = tabAdapter
        TabLayoutMediator(bind.tabLayout, bind.viewPager2) { tabLayout, position ->
            tabLayout.text = getText(tabTitle[position])
        }.attach()  // JANGAN LUPA ATTACH
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun bindNumber(num: Int, decimalFormat: DecimalFormat): String {
        var length: Int = num.toString().length - 1
        var result: Double = num.toDouble()
        println(result)
        if (length > 3) {
            var index = 1
            while (length >= 3) {
                index *= 1000
                length -= 3
            }
            result = num.toDouble() / index
            println(result)
        }
        return decimalFormat.format(result)
    }

    private fun groupNum(num: Int, decimalFormat: DecimalFormat): String = when {
        num.toString().length - 1 >= 9 -> getString(R.string.Bval, bindNumber(num, decimalFormat))
        num.toString().length - 1 in 6..8 -> getString(
            R.string.Mval,
            bindNumber(num, decimalFormat)
        )
        num.toString().length - 1 in 4..5 -> getString(
            R.string.Kval,
            bindNumber(num, decimalFormat)
        )
        else -> decimalFormat.format(num)
    }

    private fun additionalInformation(information: String?, text: TextView) {
        if (information == null) text.visibility = View.GONE
        else text.text = information
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}