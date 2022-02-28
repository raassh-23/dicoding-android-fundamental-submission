package com.raassh.dicodinggithubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raassh.dicodinggithubuserapp.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var user: User

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User
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
            name,
            location,
            repositoryCount,
            company,
            followersCount,
            followingCount,
            avatar
        ) = user

        binding.detailsAvatar.setImageResource(avatar)
        binding.tvDetailsName.text = name
        binding.tvDetailsUsername.text = username
        binding.tvDetailsCompany.text = getString(R.string.company, company)
        binding.tvDetailsLocation.text = getString(R.string.location, location)
        binding.tvDetailsRepoCount.text = repositoryCount.toString()
        binding.tvDetailsFollowerCount.text = followersCount.toString()
        binding.tvDetailsFollowingCount.text = followingCount.toString()
    }
}