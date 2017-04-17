package com.xiaoyezi.midicore.factorytool.log.adapter;

import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoyezi.midicore.factorytool.R;
import com.xiaoyezi.midicore.factorytool.data.FileModel;
import com.xiaoyezi.midicore.factorytool.log.adapter.itemtouchhelper.ItemTouchHelperAdapter;
import com.xiaoyezi.midicore.factorytool.log.adapter.itemtouchhelper.ItemTouchHelperViewHolder;
import com.xiaoyezi.midicore.factorytool.utils.Tlog;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by jim on 2017/4/14.
 */
public class LogsAdapter extends RecyclerView.Adapter <LogsAdapter.LogsHolder> implements ItemTouchHelperAdapter {
    private List<FileModel> mLogs;

    private ItemTouchHelper mItemTouchHelper;

    public LogsAdapter(List<FileModel> logs) {
        mLogs = logs;
    }

    /**
     * Set touch helper
     *
     * @param itemTouchHelper
     */
    public void setTouchHelper(ItemTouchHelper itemTouchHelper) {
        mItemTouchHelper = itemTouchHelper;
    }

    @Override
    public LogsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);
        return new LogsHolder(view);
    }

    @Override
    public void onBindViewHolder(final LogsHolder holder, final int position) {
        FileModel log = mLogs.get(position);

        holder.fileName.setText(log.getName());
        holder.fileSize.setText(String.valueOf(log.getSize()));
        holder.fileTime.setText(log.getCreatedTime());

        holder.fileHandleIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    mItemTouchHelper.startDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLogs.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mLogs, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        deleteFile(position);

        notifyItemRemoved(position);
    }

    /**
     * Delete log file
     *
     * @param position
     */
    private void deleteFile(int position) {
        if (position >= 0 && position < mLogs.size()) {
            FileModel fileModel = mLogs.get(position);
            if (fileModel != null) {
                (new File(fileModel.getPath() + fileModel.getName())).delete();
            }
        }

        mLogs.remove(position);

        if (mLogs.size() == 0) {
            Tlog.resetLogName();
        }
    }

    /**
     * Log view holder
     */
    public static class LogsHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        TextView fileName;
        TextView filePath;
        TextView fileSize;
        TextView fileTime;
        ImageView fileIcon;
        ImageView fileHandleIcon;

        public LogsHolder(View itemView) {
            super(itemView);

            fileName = (TextView)itemView.findViewById(R.id.file_name);
            fileSize = (TextView)itemView.findViewById(R.id.file_size);
            fileTime = (TextView)itemView.findViewById(R.id.file_time);
            fileIcon = (ImageView)itemView.findViewById(R.id.file_image);
            fileHandleIcon = (ImageView)itemView.findViewById(R.id.handle);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
