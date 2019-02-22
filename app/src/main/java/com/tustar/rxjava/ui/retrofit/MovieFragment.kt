package com.tustar.rxjava.ui.retrofit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tustar.rxjava.R
import com.tustar.rxjava.util.Logger
import com.tustar.rxjava.util.plus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MovieFragment : Fragment() {

    private val mDisposables = CompositeDisposable()
    private val mLayoutManager = LinearLayoutManager(context)
    private lateinit var mAdapter: MovieRecyclerViewAdapter
    private var mMovies = arrayListOf<String>()
    private var mStart = 0
    private var mLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_retrofit, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = mLayoutManager
                addItemDecoration(DividerItemDecoration(context, mLayoutManager.orientation))
                mAdapter = MovieRecyclerViewAdapter(mMovies)
                adapter = mAdapter
                itemAnimator = DefaultItemAnimator()

                addOnScrollListener(object : RecyclerView.OnScrollListener() {

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition()
                        if (lastVisiblePosition == mMovies.size - 1) {
                            getMovie(mStart)
                        }
                    }
                })
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMovie(mStart)
    }

    private fun getMovie(start: Int, count: Int = 10) {
        Logger.i("start = $start")
        if (mLoading) {
            return
        }

        mDisposables + MovieService.create().listTop250(start, count)
                .doOnSubscribe {
                    mLoading = true
                }
                .subscribeOn(Schedulers.io())
                .map {
                    it.subjects.map { subject -> subject.title }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mMovies.addAll(it)
                    mStart += count
                    mAdapter.submitList(mMovies)
                    mMovies.forEachIndexed { index, s -> Logger.d("${index + 1} $s") }
                    mAdapter.notifyDataSetChanged()
                    mLoading = false
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.clear()
    }


    companion object {

        @JvmStatic
        fun newInstance() = MovieFragment()
    }
}
