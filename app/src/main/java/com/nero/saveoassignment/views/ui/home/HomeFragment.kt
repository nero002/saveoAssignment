package com.nero.saveoassignment.views.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.nero.saveoassignment.data.model.MovieResponseItem
import com.nero.saveoassignment.data.remote.MovieClickListener
import com.nero.saveoassignment.databinding.FragmentHomeBinding
import com.nero.saveoassignment.viewmodel.MovieViewModel
import com.nero.saveoassignment.views.ui.home.rv.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), MovieClickListener {


    private var _binding: FragmentHomeBinding? = null
    private var movieList = mutableListOf<MovieResponseItem>()
    val viewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var navController: NavController

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        val llManager = GridLayoutManager(requireContext(), 3)
        binding.rvMovies.layoutManager = llManager
        movieAdapter = MovieAdapter(this)

        viewModel.movie.observe(viewLifecycleOwner, Observer {
            viewLifecycleOwner.lifecycleScope.launch {

                movieAdapter.submitData(it)

            }
        })

        binding.rvMovies.adapter = movieAdapter

        viewModel.movie().observe(viewLifecycleOwner, Observer {
            movieList.addAll(it)
            movieAdapter.notifyDataSetChanged()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMovieClicked(movieResponse: Int?) {
        TODO("Not yet implemented")
    }
}