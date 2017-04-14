package com.xiaoyezi.midicore.factorytool.log.adapter;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoyezi.midicore.factorytool.data.FileModel;
import com.xiaoyezi.midicore.factorytool.R;

/**
 * Created by jim on 2017/4/14.
 */
public class LogsAdapter extends RecyclerView.Adapter <LogsAdapter.LogsHolder>{
    private List<FileModel> mLogs;

    OnItemClickListener mOnItemClickListener;

    public LogsAdapter(List<FileModel> logs) {
        mLogs = logs;
    }

    @Override
    public LogsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);
        return new LogsHolder(view);
    }

    @Override
    public void onBindViewHolder(LogsHolder holder, final int position) {
        FileModel log = mLogs.get(position);

        holder.fileName.setText(log.getName());
        holder.fileSize.setText(String.valueOf(log.getSize()));
        holder.fileTime.setText(log.getCreatedTime());

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onLongClick(position);

                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mLogs.size();
    }

    /**
     * item click listener
     */
    public interface OnItemClickListener{
        void onClick( int position);
        void onLongClick( int position);
    }

    /**
     * Set item listener
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * Log view holder
     */
    public class LogsHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        TextView filePath;
        TextView fileSize;
        TextView fileTime;
        ImageView fileIcon;
        ImageView fileMoreIcon;

        public LogsHolder(View itemView) {
            super(itemView);

            fileName = (TextView)itemView.findViewById(R.id.file_name);
            fileSize = (TextView)itemView.findViewById(R.id.file_size);
            fileTime = (TextView)itemView.findViewById(R.id.file_time);
            fileIcon = (ImageView)itemView.findViewById(R.id.file_image);
            fileMoreIcon = (ImageView)itemView.findViewById(R.id.file_more);
        }
    }
}
