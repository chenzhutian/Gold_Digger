/**
 * @file TradingRecordListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.billme.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.billme.ui.R;

public class TradingRecordListView extends ExpandableListView implements
		OnScrollListener {

	/**
	 * Adapter interface. The list adapter must implement this interface.
	 */
	public interface PinnedHeaderAdapter {

		/**
		 * Pinned header state: don't show the header.
		 */
		public static final int PINNED_HEADER_GONE = 0;

		/**
		 * Pinned header state: show the header at the top of the list.
		 */
		public static final int PINNED_HEADER_VISIBLE = 1;

		/**
		 * Pinned header state: show the header. If the header extends beyond
		 * the bottom of the first shown element, push it up and clip.
		 */
		public static final int PINNED_HEADER_PUSHED_UP = 2;

		/**
		 * Computes the desired state of the pinned header for the given
		 * position of the first visible list item. Allowed return values are
		 * {@link #PINNED_HEADER_GONE}, {@link #PINNED_HEADER_VISIBLE} or
		 * {@link #PINNED_HEADER_PUSHED_UP}.
		 */
		int getPinnedHeaderState(int groupPosition, int childPosition);

		/**
		 * Configures the pinned header view to match the first visible list
		 * item.
		 * 
		 * @param header
		 *            pinned header view.
		 * @param position
		 *            position of the first visible list item.
		 * @param alpha
		 *            fading of the header view, between 0 and 255.
		 */
		void configurePinnedHeader(View header, int groupPosition,
				int childPositon, int alpha);

	}

	// -- pinned Header view
	private static final int MAX_ALPHA = 255;
	private PinnedHeaderAdapter nAdapter;
	public View nHeaderView;
	private boolean nHeaderViewVisible;
	private int nHeaderViewWidth;
	private int nHeaderViewHeight;
	private int nOldState = -1;

	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back

	// the interface to trigger refresh and load more.
	private IXListViewListener mListViewListener;

	// -- header view
	private TradingRecordListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	private TextView mHeaderTimeView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private TradingRecordListViewFooter mFooterView;
	private boolean mEnablePullLoad = true;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
														// at bottom, trigger
														// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.

	/**
	 * @param context
	 */
	public TradingRecordListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public TradingRecordListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public TradingRecordListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// TradingRecordListView need the scroll event, and it will dispatch the
		// event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new TradingRecordListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
		mHeaderTimeView = (TextView) mHeaderView
				.findViewById(R.id.tv_TRLVheader_time);
		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new TradingRecordListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});

		// init pinned View
		LayoutInflater inflater = LayoutInflater.from(context);
		nHeaderView = inflater.inflate(R.layout.trading_record_listview_group,
				this, false);
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		nHeaderView.setLayoutParams(lp);
		if (nHeaderView != null) {
			setFadingEdgeLength(0);
		}
		requestLayout();

	}

	@Override
	public void setAdapter(ExpandableListAdapter adapter) {
		// make sure TradingRecordListViewFooter is the last footer view, and
		// only add once.
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
		nAdapter = (PinnedHeaderAdapter) adapter;
		setOnScrollListener(this);
	}

	/**
	 * for section list header
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (nHeaderView != null) {
			measureChild(nHeaderView, widthMeasureSpec, heightMeasureSpec);
			nHeaderViewWidth = nHeaderView.getMeasuredWidth();
			nHeaderViewHeight = nHeaderView.getMeasuredHeight();
		}
	}

	/**
	 * for section list header
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
		final int groupPos = ExpandableListView
				.getPackedPositionGroup(flatPostion);
		final int childPos = ExpandableListView
				.getPackedPositionChild(flatPostion);
		int state = nAdapter.getPinnedHeaderState(groupPos, childPos);
		if (nHeaderView != null && nAdapter != null && state != nOldState) {
			nOldState = state;
			nHeaderView.layout(0, 0, nHeaderViewWidth, nHeaderViewHeight);
		}
		configureHeaderView(groupPos, childPos);
	}

	/**
	 * for section list header
	 */
	public void configureHeaderView(int groupPosition, int childPosition) {
		if (nHeaderView == null || nAdapter == null
				|| ((ExpandableListAdapter) nAdapter).getGroupCount() == 0) {
			return;
		}

		int state = nAdapter.getPinnedHeaderState(groupPosition, childPosition);

		switch (state) {
		case PinnedHeaderAdapter.PINNED_HEADER_GONE: {
			nHeaderViewVisible = false;
			break;
		}
		case PinnedHeaderAdapter.PINNED_HEADER_VISIBLE: {
			nAdapter.configurePinnedHeader(nHeaderView, groupPosition,
					childPosition, MAX_ALPHA);
			if (nHeaderView.getTop() != 0) {
				nHeaderView.layout(0, 0, nHeaderViewWidth, nHeaderViewHeight);
			}
			nHeaderViewVisible = true;
			break;
		}
		case PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP: {
			View firstView = getChildAt(0);
			int bottom = firstView.getBottom();
			// int itemHeight = firstView.getHeight();
			int headerHeight = nHeaderView.getHeight();
			int y;
			// int alpha;
			if (bottom < headerHeight) {
				y = (bottom - headerHeight);
				// alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
			} else {
				y = 0;
				// alpha = MAX_ALPHA;
			}
			nAdapter.configurePinnedHeader(nHeaderView, groupPosition,
					childPosition, 0);
			if (nHeaderView.getTop() != y) {
				nHeaderView.layout(0, y, nHeaderViewWidth, nHeaderViewHeight
						+ y);
			}
			nHeaderViewVisible = true;
			break;
		}
		}
	}

	/**
	 * for section list header
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (nHeaderViewVisible) {
			drawChild(canvas, nHeaderView, getDrawingTime());
		}
	}

	/**
	 * enable or disable pull down refresh feature.
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * enable or disable pull up load more feature.
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(TradingRecordListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(TradingRecordListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * set last refresh time
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) {
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(TradingRecordListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(TradingRecordListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.setState(TradingRecordListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(TradingRecordListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(TradingRecordListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	private float nDownX;
	private float nDownY;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			if (nHeaderViewVisible) {
				nDownX = ev.getX();
				nDownY = ev.getY();
				if (nDownX <= nHeaderViewWidth && nDownY <= nHeaderViewHeight) {
					return true;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (getFirstVisiblePosition() == 0
					&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				// the first item is showing, header has shown or pull down.
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				// invokeOnScrolling();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				// last item, already pulled up or want to pull up.
				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (nHeaderViewVisible) {
				float x = ev.getX();
				float y = ev.getY();
				float offsetX = Math.abs(x - nDownX);
				float offsetY = Math.abs(y - nDownY);
				if (x <= nHeaderViewWidth && y <= nHeaderViewHeight
						&& offsetX <= nHeaderViewWidth
						&& offsetY <= nHeaderViewHeight) {
					if (nHeaderView != null) {
						nheaderViewClick();
					}
					return true;
				}
			}
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				if (mEnablePullRefresh
						&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					mPullRefreshing = true;
					mHeaderView
							.setState(TradingRecordListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						mListViewListener.onRefresh();
					}
				}
				resetHeaderHeight();
			}
			if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more.
				if (mEnablePullLoad
						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void nheaderViewClick() {
		long packedPosition = getExpandableListPosition(this
				.getFirstVisiblePosition());
		int groupPosition = ExpandableListView
				.getPackedPositionGroup(packedPosition);

		if (this.isGroupExpanded(groupPosition)) {
			this.collapseGroup(groupPosition);
		} else {
			this.expandGroup(groupPosition);
		}

		this.setSelectedGroup(groupPosition);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
		}
		super.computeScroll();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		final long flatPos = getExpandableListPosition(firstVisibleItem);
		int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
		int childPosition = ExpandableListView.getPackedPositionChild(flatPos);

		configureHeaderView(groupPosition, childPosition);
	}

	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}

}
