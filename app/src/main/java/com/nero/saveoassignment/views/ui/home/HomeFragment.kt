package com.nero.saveoassignment.views.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nero.saveoassignment.data.model.MovieResponseItem
import com.nero.saveoassignment.data.remote.MovieClickListener
import com.nero.saveoassignment.databinding.FragmentHomeBinding
import com.nero.saveoassignment.databinding.ItemLayoutBinding
import com.nero.saveoassignment.viewmodel.MovieViewModel
import com.nero.saveoassignment.views.MovieActivity
import com.nero.saveoassignment.views.ui.home.rvHorizontal.HorizontalMovieAdapter
import com.nero.saveoassignment.views.ui.home.rvVertical.MovieAdapter
import com.skydoves.transformationlayout.onTransformationStartContainer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), MovieClickListener {


    private var _binding: FragmentHomeBinding? = null
    private var movieList = mutableListOf<MovieResponseItem>()
    val viewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var navController: NavController
    private lateinit var horizontalMovieAdapter: HorizontalMovieAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        verticalRV()
        horizontalRV()


    }


    //observing  & setting up a data for horizontal RecyclerView
    private fun horizontalRV() {
        val llManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        horizontalMovieAdapter = HorizontalMovieAdapter(movieList, this)

        binding.rvHorizontal.apply {
            layoutManager = llManager
            adapter = horizontalMovieAdapter
        }


        viewModel.movie().observe(viewLifecycleOwner, Observer {

            //shrimming effect
            binding.apply {
                shimmerFrameLayoutHorizontal.stopShimmerAnimation()
                shimmerFrameLayoutHorizontal.visibility = View.GONE
                rvHorizontal.visibility = View.VISIBLE
            }
            movieList.addAll(it)
            horizontalMovieAdapter.notifyDataSetChanged()
        })


    }

    //observing  & setting a data for Vertical RecyclerView
    private fun verticalRV() {
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
            //shrimming effect
            binding.apply {
                shimmerFrameLayoutVertical.stopShimmerAnimation()
                shimmerFrameLayoutVertical.visibility = View.GONE
                rvMovies.visibility = View.VISIBLE
            }
            movieList.addAll(it)
            movieAdapter.notifyDataSetChanged()
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    //sending the movieResponse details on click of movie
    override fun onMovieClicked(movieResponse: MovieResponseItem) {


        val action = HomeFragmentDirections.actionNavHomeToMovieDetailsFragment(
            movieResponse
        )

        navController.navigate(action)


    }


}