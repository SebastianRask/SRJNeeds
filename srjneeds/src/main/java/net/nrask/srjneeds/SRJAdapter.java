package net.nrask.srjneeds;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian Rask on 18-04-2017.
 */

public class SRJAdapter<E, VH extends SRJViewHolder<E>> extends RecyclerView.Adapter<VH>  {
	@LayoutRes
	protected int mLayout;
	protected List<E> mItems;
	protected ViewHolderFactory<VH> mViewHolderCreator;
	protected ItemCallback<VH> mItemCallback;

	public SRJAdapter(int mLayout, List<E> mItems, ViewHolderFactory<VH> mViewHolderCreator, ItemCallback<VH> mItemCallback) {
		this.mLayout = mLayout;
		this.mItems = mItems;
		this.mViewHolderCreator = mViewHolderCreator;
		this.mItemCallback = mItemCallback;
	}

	public SRJAdapter(int mLayout, ViewHolderFactory<VH> mViewHolderCreator) {
		this.mItems = new ArrayList<>();
		this.mLayout = mLayout;
		this.mViewHolderCreator = mViewHolderCreator;
	}

	@Override
	public VH onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
		return mViewHolderCreator.create(itemView);
	}

	@Override
	public void onBindViewHolder(final VH holder, int position) {
		E item = mItems.get(position);
		holder.bindObject(item);

		if (mItemCallback != null) {
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mItemCallback.onItemClicked(view, holder);
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return mItems == null ? 0 : mItems.size();
	}

	public void addItem(E item, int position) {
		mItems.add(position, item);
		notifyItemInserted(position);
	}

	public void addItem(E item) {
		int position = getItemCount();
		mItems.add(position, item);
		notifyItemInserted(position);
	}

	public void addItems(List<E> items) {
		int startPosition = getItemCount();
		mItems.addAll(items);
		notifyItemRangeInserted(startPosition, items.size());
	}

	public E removeItem(int position) {
		E item = mItems.get(position);
		mItems.remove(position);
		notifyItemRemoved(position);

		return item;
	}

	public E removeItem(E item) {
		int position = mItems.indexOf(item);
		mItems.remove(position);
		notifyItemRemoved(position);
		
		return item;
	}

	public interface ViewHolderFactory<VH> {
		VH create(View itemView);
	}

	public interface ItemCallback<VH> {
		void onItemClicked(View clickedView, VH viewHolder);
	}
}
