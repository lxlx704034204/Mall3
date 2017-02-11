//package com.hxqc.mall.photolibrary.fragment;
//
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.GridView;
//
//import com.hxqc.mall.core.R;
//import com.hxqc.mall.photolibrary.adapter.GridPhotoAdapter;
//import com.hxqc.mall.photolibrary.control.GalleryControl;
//import com.hxqc.mall.photolibrary.model.MediaImgEntity;
//import com.hxqc.mall.photolibrary.util.FileUtil;
//import com.hxqc.mall.photolibrary.util.Imagehelper;
//import com.hxqc.widget.PhotoChooseFragment;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class GridPhotoFragment extends PhotoChooseFragment implements AdapterView.OnItemClickListener {
//
//	GridPhotoAdapter gridPhotoAdapter;
//	GridView mGridView;
//	GalleryControl mControl;
//
//	public GridPhotoFragment() {
//		// Required empty public constructor
//	}
//
//	@Override
//	public void chooseSuccess(String filePath) {
//		MediaImgEntity entity = new MediaImgEntity(filePath);
//		mControl.checkChange(true, entity);
//		getActivity().finish();
//	}
//
//	@Override
//	public boolean toCropPhoto() {
//		return false;
//	}
//
//	@Override
//	public String getCameraPath() {
//		return FileUtil.getCameraPath(getActivity());
//	}
//
//	@Override
//	protected String getCropCacheFilePath() {
//		return null;
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		mControl = GalleryControl.getDefault(getActivity());
////        initLoadingOption();
//	}
//
//	private void initLoadingOption() {
////        DisplayImageOptions options = new DisplayImageOptions.Builder()
////                .resetViewBeforeLoading(true).cacheInMemory(true).bitmapConfig(Bitmap.Config.ARGB_4444)
////                .showImageOnLoading(R.drawable.bg_imgloading).delayBeforeLoading(1000)
////                .build();
////        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
////                getActivity())
////                .tasksProcessingOrder(QueueProcessingType.FIFO)
////                .defaultDisplayImageOptions(options).build();
////        ImageLoader.getInstance().init(config);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//	                         Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.fragment_grid_photo, container, false);
//		mGridView = (GridView) rootView.findViewById(R.id.grid_photo_grid_view);
//
//		return rootView;
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//	}
//
//	@Override
//	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		gridPhotoAdapter = new GridPhotoAdapter(getActivity(), true);
//		mGridView.setAdapter(gridPhotoAdapter);
//		mGridView.setOnItemClickListener(this);
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		gridPhotoAdapter.notifyDataSetInvalidated();
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		if (position == 0) {
//			getPicFromCamera();
//		} else if (position > 1) {
//			int location[] = new int[2];
//			view.getLocationOnScreen(location);
//			Bundle bundle = new Bundle();
//			bundle.putInt("locationX", location[0]);
//			bundle.putInt("locationY", location[1]);
//			bundle.putInt("width", view.getWidth());
//			bundle.putInt("height", view.getHeight());
//			Imagehelper.toPreview(getActivity(), position - 1, false, bundle);
//		}
//	}
//
//	public void toPreview() {
//		Imagehelper.toPreview(getActivity(), 0, true, null);
//	}
//
//}
