package net.nrask.srjneeds;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Sebastian Rask on 18-04-2017.
 */

public class SRJViewHolder<T> extends RecyclerView.ViewHolder {
	protected T data;

	public void bindObject(T object) {
		data = object;
	}

	public SRJViewHolder(View itemView) {
		super(itemView);
	}
}