package com.raassh.dicodinggithubuserapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raassh.dicodinggithubuserapp.R
import com.raassh.dicodinggithubuserapp.adapter.ListUserAdapter
import com.raassh.dicodinggithubuserapp.api.ListUsersResponse
import com.raassh.dicodinggithubuserapp.databinding.FragmentFollowBinding
import com.raassh.dicodinggithubuserapp.misc.createUserArrayList
import com.raassh.dicodinggithubuserapp.misc.visibility
import com.raassh.dicodinggithubuserapp.viewmodel.FollowViewModel

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val followViewModel by viewModels<FollowViewModel> {
        FollowViewModel.Factory(
            arguments?.getString(ARG_USERNAME) ?: "",
            arguments?.getInt(ARG_SECTION_NUMBER) ?: 0
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.apply {
            rvUsers.apply {
                setHasFixedSize(true)
                this.layoutManager = layoutManager
                addItemDecoration(
                    DividerItemDecoration(
                        requireActivity(),
                        layoutManager.orientation
                    )
                )
            }

            btnRetry.setOnClickListener { followViewModel.getFollowUsers() }

            emptyText.text = getString(R.string.empty_text, followViewModel.followType)
        }

        followViewModel.apply {
            listUsers.observe(requireActivity()) {
                setUsersData(it)
            }

            isLoading.observe(requireActivity()) {
                showLoading(it)
            }

            error.observe(requireActivity()) {
                it.getContentIfNotHandled()?.let { text ->
                    showError(text)
                }
            }

            canRetry.observe(requireActivity()) {
                binding.btnRetry.visibility = visibility(it)
            }
        }
    }

    private fun setUsersData(listUsers: List<ListUsersResponse>) {
        val users = createUserArrayList(listUsers)

        binding.apply {
            if (users.count() == 0) {
                emptyText.visibility = visibility(true)
            } else {
                rvUsers.adapter = ListUserAdapter(users)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = visibility(isLoading)
            rvUsers.visibility = visibility(!isLoading)
        }
    }

    private fun showError(text: String) {
        Snackbar.make(binding.root, getString(R.string.error_message, text), Snackbar.LENGTH_SHORT)
            .setAction(R.string.try_again) { followViewModel.getFollowUsers() }
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    followViewModel.setCanRetry()
                }
            })
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }
}