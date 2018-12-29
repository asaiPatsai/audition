package lt.mano.audition.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lt.mano.audition.MapActivity;
import lt.mano.audition.R;
import lt.mano.audition.models.ScanDataItem;

public class ScanListAdapter extends RecyclerView.Adapter<ScanListAdapter.ScanViewHolder> {
    private Context mContext;
    private List<ScanDataItem> mScanList;

    public class ScanViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView date;

        public ScanViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.time_date);
        }
    }


    public ScanListAdapter(List<ScanDataItem> scanList, Context context) {
        mScanList = scanList;
        mContext = context;
    }

    @NonNull
    @Override
    public ScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scan_list_item, parent, false);

        return new ScanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScanViewHolder holder, final int position) {
        ScanDataItem scanItem = mScanList.get(position);
        if (!scanItem.name.isEmpty()) {
            holder.title.setText(scanItem.name);
        } else {
            holder.title.setText("Unknown name");
        }
        holder.date.setText(scanItem.date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MapActivity)mContext).handleSelection(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mScanList != null) {
            return mScanList.size();
        } else {
            return 0;
        }
    }
}
