package fasttrack.jdeveloper.popularmoviesapp.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fasttrack.jdeveloper.popularmoviesapp.R;
import fasttrack.jdeveloper.popularmoviesapp.models.Trailer;

/**
 * Created by jdeveloper on 20/1/17.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private ArrayList<Trailer> trailers;
    final private ListItemClickListener mOnClickListener;

    public TrailersAdapter(ArrayList<Trailer> trailers, ListItemClickListener listener) {
        this.trailers = trailers;
        mOnClickListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.cell_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(trailers.get(position));
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public void updateList(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView trailerName;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerName = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        public void bind(Trailer trailer) {
            trailerName.setText(trailer.getName());
        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int position);
    }
}
