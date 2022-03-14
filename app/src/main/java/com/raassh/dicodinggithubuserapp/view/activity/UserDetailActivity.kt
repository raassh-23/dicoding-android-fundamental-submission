package com.raassh.dicodinggithubuserapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.raassh.dicodinggithubuserapp.R
import com.raassh.dicodinggithubuserapp.adapter.FollowSectionsPagerAdapter
import com.raassh.dicodinggithubuserapp.api.UserDetailResponse
import com.raassh.dicodinggithubuserapp.misc.UserItem
import com.raassh.dicodinggithubuserapp.databinding.ActivityUserDetailBinding
import com.raassh.dicodinggithubuserapp.misc.visibility
import com.raassh.dicodinggithubuserapp.viewmodel.UserDetailViewModel

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private val userDetailViewModel by viewModels<UserDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<UserItem>(EXTRA_USER) as UserItem
        userDetailViewModel.apply {
            getUserDetail(user.username)

            this.user.observe(this@UserDetailActivity) {
                showDetails(it)
            }

            isLoading.observe(this@UserDetailActivity) {
                showLoading(it)
            }

            error.observe(this@UserDetailActivity) {
                it.getContentIfNotHandled()?.let { text ->
                    showError(text, user.username)
                }
            }
        }

        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnShare.setOnClickListener {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, user.toString())
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            btnRetry.setOnClickListener { userDetailViewModel.getUserDetail(user.username) }
        }

        val viewPager = binding.followViewPager.apply {
            adapter = FollowSectionsPagerAdapter(this@UserDetailActivity, user.username)
        }

        val tabs = binding.followTabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = getString(TAB_TITLES[position])

        }.attach()

    }

    private fun showError(text: String, username: String) {
        Snackbar.make(binding.root, getString(R.string.error_message, text), Snackbar.LENGTH_SHORT)
            .setAction(R.string.try_again) { userDetailViewModel.getUserDetail(username) }
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    binding.btnRetry.visibility = visibility(true)
                }
            })
            .show()
    }

    private fun showDetails(user: UserDetailResponse) {
        with(user) {
            binding.apply {
                Glide.with(this@UserDetailActivity)
                    .load(avatarUrl)
                    .circleCrop()
                    .into(detailsAvatar)
                tvDetailsName.text = name
                tvDetailsUsername.text = login
                tvDetailsCompany.text = getString(R.string.company, company)
                tvDetailsLocation.text = getString(R.string.location, location)
                tvDetailsRepoCount.text = publicRepos.toString()
                tvDetailsFollowerCount.text = followers.toString()
                tvDetailsFollowingCount.text = following.toString()

                detailsStatic.visibility = visibility(true)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = visibility(isLoading)
            detailsContent.visibility = visibility(!isLoading)

            if (isLoading) {
                btnRetry.visibility = visibility(false)
                detailsStatic.visibility = visibility(false)
            }
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follow_tab_1,
            R.string.follow_tab_2
        )
    }
}