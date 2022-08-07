
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.runwithme.databinding.ItemCrewRankingBinding
import com.ssafy.runwithme.model.response.RankingResponse
import com.ssafy.runwithme.view.crew_detail.ranking.CrewUserRankingListener
import com.ssafy.runwithme.view.crew_detail.ranking.CrewUserRankingViewModel


class CrewUserRankingAdapter(private val crewUserRankingViewModel: CrewUserRankingViewModel, private val crewUserRankingListener : CrewUserRankingListener) : ListAdapter<RankingResponse, CrewUserRankingAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(private val binding: ItemCrewRankingBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(crewRank: RankingResponse){
            binding.crewUserRankingVM = crewUserRankingViewModel
            binding.crewRank = crewRank
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                crewUserRankingListener.onItemClick(getItem(adapterPosition).userSeq)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCrewRankingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<RankingResponse>(){
            override fun areItemsTheSame(oldItem: RankingResponse, newItem: RankingResponse): Boolean {
                return oldItem.userSeq == newItem.userSeq
            }

            override fun areContentsTheSame(oldItem: RankingResponse, newItem: RankingResponse): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}