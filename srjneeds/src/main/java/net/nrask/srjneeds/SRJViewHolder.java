package net.nrask.srjneeds;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Sebastian Rask on 18-04-2017.
 */

public abstract class SRJViewHolder<T> extends RecyclerView.ViewHolder {
	protected T data;

	public void bindObject(T object) {
		data = object;
		onDataBinded(data);
	}

	public SRJViewHolder(View itemView) {
		super(itemView);
	}

	public T getData() {
		return data;
	}

	protected abstract void onDataBinded(T data);
}