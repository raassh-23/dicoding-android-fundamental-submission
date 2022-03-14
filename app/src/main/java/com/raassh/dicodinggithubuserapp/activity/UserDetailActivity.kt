package com.raassh.dicodinggithubuserapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.raassh.dicodinggithubuserapp.misc.UserItem
import com.raassh.dicodinggithubuserapp.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var user: UserItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<UserItem>(EXTRA_USER) as UserItem
        showDetails()

        binding.btnBack.setOnClickListener { finish() }
        binding.btnShare.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, user.toString())
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun showDetails() {
        val (
            username,
            avatar
        ) = user

        binding.apply {
            Glide.with(this@UserDetailActivity)
                .load(avatar)
                .into(detailsAvatar)
            tvDetailsUsername.text = username
        }
    }

//    private fun showDetails() {
//        val (
//            username,
//            name,
//            location,
//            repositoryCount,
//            company,
//            followersCount,
//            followingCount,
//            avatar
//        ) = user
//
//        binding.apply {
//            detailsAvatar.setImageResource(avatar)
//            tvDetailsName.text = name
//            tvDetailsUsername.text = username
//            tvDetailsCompany.text = getString(R.string.company, company)
//            tvDetailsLocation.text = getString(R.string.location, location)
//            tvDetailsRepoCount.text = repositoryCount.toString()
//            tvDetailsFollowerCount.text = followersCount.toString()
//            tvDetailsFollowingCount.text = followingCount.toString()
//        }
//    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}