package com.example.playlistmaker.playlistInfo.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.playlistEdit.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SearchFragment.Companion.CLICK_TRACK_DEBOUNCE_DELAY
import com.example.playlistmaker.search.ui.SearchTracksAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class PlaylistInfoFragment : Fragment() {

    private val viewModel by viewModel<PlaylistInfoViewModel>()
    private var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding!!

    lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<LinearLayout>
    lateinit var playlistSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var isClickAllowed = true

    private val tracksAdapter = SearchTracksAdapter(
        clickListener = { track ->
            if (clickDebounce()) {
                val action = PlaylistInfoFragmentDirections.actionPlaylistInfoFragmentToAudioPlayerFragment(track)
                findNavController().navigate(action)
                viewModel.onClickTrack(track)
            }
        },
        longClickListener = { track ->
            showDeleteTrackDialog(track)
            true
        }
    )

    private var bottomPadding = 0



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)


        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observePlaylistDuration().observe(viewLifecycleOwner) {
            binding.tvPlaylistDuration.text =
                requireContext().resources.getQuantityString(R.plurals.minutes, it, it)
        }
        viewModel.observeTracks().observe(viewLifecycleOwner){
            showTracks(it)
        }
        viewModel.observeShowMessage().observe(viewLifecycleOwner){
            showToast(it)
        }
        viewModel.observeShowDeletePlaylistDialog().observe(viewLifecycleOwner){
            showDeletePlaylistDialog(it)
        }
        viewModel.observeShowBottomSheetMenu().observe(viewLifecycleOwner){
            showBottomSheet(it)
        }
        viewModel.observeOpenEditPlaylist().observe(viewLifecycleOwner){
            openEditPlaylist(it)
        }

        viewModel.initPlaylist()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.playlistInfoFragment) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            bottomPadding = systemBars.bottom
            view.setPadding(0, 0, 0, bottomPadding)
            insets
        }

        binding.rvPlayerPlaylists.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlayerPlaylists.adapter = tracksAdapter

        binding.overlay.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.bottomSheetBehaviorMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehaviorMenu.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = 1 + slideOffset
            }

        })

        playlistSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet)

        binding.llPlaylist.doOnNextLayout {
            val playlistInfoFragmentHeight = binding.playlistInfoFragment.height
            val llPlaylistHeight = binding.llPlaylist.height
            val peekHeight = playlistInfoFragmentHeight - llPlaylistHeight
            playlistSheetBehavior.peekHeight = peekHeight

        }

        binding.playlistInfoToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.imgShare.setOnClickListener {
            viewModel.onClickShare()
        }
        binding.imgMore.setOnClickListener {
            viewModel.onClickMore()
        }
        binding.tvBtnMenuSharePlaylist.setOnClickListener {
            viewModel.onClickShare()
        }
        binding.tvBtnMenuEditPlaylist.setOnClickListener {
            viewModel.onClickMenuEditPlaylist()
        }
        binding.tvBtnMenuDeletePlaylist.setOnClickListener {
            viewModel.onClickMenuDeletePlaylist()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_TRACK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun render(state: PlaylistInfoState) {
        when (state) {
            is PlaylistInfoState.Content -> showPlaylist(state.playlist)
            PlaylistInfoState.Close -> close()
        }
    }

    fun showPlaylist(playlist: Playlist) {
        Glide
            .with(binding.root)
            .load(playlist.pathImageCover)
            .placeholder(R.drawable.ic_track_placeholder_312)
            .into(binding.playlistImg)
        binding.tvPlaylistName.text = playlist.name
        if (!playlist.description.isNullOrEmpty()){
            binding.tvPlaylistDescription.text = playlist.description
        }
        if (playlist.numberTracks == null){
            binding.tvTracksNumber.text =
                ContextCompat.getString(requireContext(), R.string.text_tracks_null)
        }else{
            binding.tvTracksNumber.text = requireContext().resources.getQuantityString(R.plurals.tracks, playlist.numberTracks, playlist.numberTracks)
        }
        Glide
            .with(binding.root)
            .load(playlist.pathImageCover)
            .placeholder(R.drawable.ic_track_placeholder_45)
            .into(binding.imgMenuPlaylist)
        binding.tvMenuPlaylistName.text = playlist.name
        if (playlist.numberTracks == null){
            binding.tvMenuPlaylistCount.text =
                ContextCompat.getString(requireContext(), R.string.text_tracks_null)
        }else{
            binding.tvMenuPlaylistCount.text = requireContext().resources.getQuantityString(R.plurals.tracks, playlist.numberTracks, playlist.numberTracks)
        }
    }

    fun showTracks(tracks: List<Track>?){
        if (tracks == null){
            binding.tvTracksEmpty.isVisible = true
            tracksAdapter.tracks.clear()
            tracksAdapter.notifyDataSetChanged()

        }else{
            binding.tvTracksEmpty.isVisible = false
            tracksAdapter.tracks.clear()
            tracksAdapter.tracks.addAll(tracks)
            tracksAdapter.notifyDataSetChanged()
        }

    }

    fun showDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialAlertDialog)
            .setMessage("Хотите удалить трек?")
            .setNegativeButton("Нет"){ _, _ ->

            }
            .setPositiveButton("Да") { _, _ ->
                viewModel.onClickYesDeleteTrack(track)
            }
            .show()
    }
    fun showDeletePlaylistDialog(playlist: Playlist) {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialAlertDialog)
            .setMessage("Хотите удалить плейлист ${playlist.name}?")
            .setNegativeButton("Нет"){ _, _ ->

            }
            .setPositiveButton("Да") { _, _ ->
                viewModel.onClickYesDeletePlaylist(playlist)
//                findNavController().navigateUp()
            }
            .show()
    }

    fun showBottomSheet(isShow: Boolean) {
        if (isShow){
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.overlay.isVisible = true
        }else{
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
            binding.overlay.isVisible = false
        }
    }

    fun openEditPlaylist(playlistId: Long){
        val action = PlaylistInfoFragmentDirections.actionPlaylistInfoFragmentToPlaylistEditFragment(playlistId)
        findNavController().navigate(action)
    }

    fun showToast(message: String) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.toast_custom, null)
        val textView = view.findViewById<TextView>(R.id.toastText)
        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_LONG
        textView.text = message
        toast.view = view
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, bottomPadding)
        toast.show()
    }

    fun close(){
        findNavController().navigateUp()
    }
}
