package com.aip.targascan.view.activity;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aip.targascan.R;
import com.aip.targascan.vo.CheckOutDataVO;
import com.aip.targascan.vo.CheckOutDataVO.ScrollToBottom;

@ContentView(R.layout.activity_check_out_detail)
public class CheckOutDetailActivity extends RoboActivity {

	@InjectView(R.id.lst) 
	private ListView mList;
	@InjectView(R.id.checkOut_txtBottomMsg)
	private TextView mTxtBottomMsg;
	@InjectView(R.id.scrollToBottomTitle)
	private TextView mTxtTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent= getIntent();
		if(intent != null)
		{
			if(intent.hasExtra("title"))
			{
				mTxtTitle.setText(intent.getStringExtra("title"));
			}
			try {
				CheckOutDataVO dataVO = (CheckOutDataVO) intent.getSerializableExtra("data");
				if(dataVO != null && dataVO.getScrollToBottom() != null && dataVO.getScrollToBottom().size() > 0)
				{
					mList.setAdapter(new AdapterData(dataVO.getScrollToBottom()));
					mTxtBottomMsg.setText(dataVO.getMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class AdapterData extends BaseAdapter {

		private List<ScrollToBottom> scrollToBottoms;

		public AdapterData(List<ScrollToBottom> scrollToBottoms) {
			this.scrollToBottoms = scrollToBottoms;
		}

		@Override
		public int getCount() {
			return scrollToBottoms.size();
		}

		@Override
		public ScrollToBottom getItem(int position) {
			return scrollToBottoms.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolderMessage holderMessage = null;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_check_out_detail, parent, false);
				holderMessage = new ViewHolderMessage();

				holderMessage.txtOrderNo = (TextView) convertView.findViewById(R.id.txtOrderNo);
				holderMessage.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
				holderMessage.txtOrderType = (TextView) convertView.findViewById(R.id.txtType);
				convertView.setTag(holderMessage);
			} else {
				holderMessage = (ViewHolderMessage) convertView.getTag();
			}

			ScrollToBottom vo = getItem(position);

			if(vo != null)
			{
				holderMessage.txtOrderNo.setText(vo.getCarton_num());
				holderMessage.txtDate.setText(vo.getDate());
				holderMessage.txtOrderType.setText(vo.getCo_type());
			}

			return convertView; 
		}

	}
	static class ViewHolderMessage {
		private TextView txtOrderNo;
		private TextView txtDate;
		private TextView txtOrderType;
	}
}
