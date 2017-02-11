package com.hxqc.mall.photolibrary.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.hxqc.mall.photolibrary.model.Album;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.model.MediaImgEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wanghao
 * Date: 2015-03-24
 * FIXME
 * Todo
 */
public class MediaImgUtil {


	/**
	 * 根据组id获取所有图片
	 *
	 * @param context
	 * @param bucker_id
	 * @return
	 */
	public static List<MediaImgEntity> getAllList3(Context context,
	                                               int bucker_id) {
		List<MediaImgEntity> galleryList = new ArrayList<>();
		ContentResolver cr = context.getContentResolver();
		String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED};
		Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
				MediaStore.Images.Media.BUCKET_ID + "= ?",
				new String[]{String.valueOf(bucker_id)}, MediaStore.Images.Media.DATE_ADDED
						+ " desc");
		if (cur != null && cur.moveToFirst()) {
			int image_id;
			String image_path;
			long date_added;
			int image_idColumn = cur.getColumnIndex(MediaStore.Images.Media._ID);
			int dPath = cur.getColumnIndex(MediaStore.Images.Media.DATA);
			int date = cur.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
			do {
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dPath);
				date_added = cur.getLong(date);
				MediaImgEntity gallery = new MediaImgEntity(image_id,
						image_path, date_added);
				galleryList.add(gallery);
			} while (cur.moveToNext());
			cur.close();
		}
		return galleryList;
	}

	/**
	 * 获取缩略图地址
	 *
	 * @param context
	 * @return
	 */
	public static List<MediaImgEntity> getThumbnailsEntities(Context context) {
		List<MediaImgEntity> galleryList = new ArrayList<>();
		ArrayList<Album> albums = ImageUtils.getAlbums(context);
		Log.d("images", albums.toString());
		for (Album data : albums) {
			ArrayList<ImageItem> imageItems = data.getImages();
			for (ImageItem item : imageItems) {
				MediaImgEntity entity = new MediaImgEntity();
				entity._id = item.getId();
				entity.img_path = item.getPath();
				entity.thumbnails_path = item.getThumbPath();
				galleryList.add(entity);
			}
		}
//        ContentResolver cr = context.getContentResolver();
//        String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.KEY_DATA};
//        Cursor cur = cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection,
//                null, null, MediaStore.Images.Thumbnails._ID + " desc");
//        if (cur.moveToFirst()) {
//            int image_id;
//            String image_path;
//            int image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
//            int dPath = cur.getColumnIndex(MediaStore.Images.Thumbnails.KEY_DATA);
//            do {
//                image_id = cur.getInt(image_idColumn);
//                image_path = cur.getString(dPath);
//                MediaImgEntity gallery = new MediaImgEntity(image_id,
//                        image_path);
//                gallery = convertFileUri(context, gallery);
//                if (gallery != null) galleryList.add(gallery);
//            } while (cur.moveToNext());
//            cur.close();
//        }
		return galleryList;
	}

	/**
	 * 缩略图id转实际图片地址
	 *
	 * @param mContext
	 * @param entity
	 */
	private synchronized static MediaImgEntity convertFileUri(Context mContext, MediaImgEntity entity) {
		String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED};
		Cursor cur = mContext
				.getContentResolver()
				.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
						MediaStore.Images.Media._ID + "=" + entity.thumbnails_id, null, null);
		if (cur != null && cur.moveToFirst()) {
			int image_id;
			String image_path;
			long date_added;
			int image_idColumn = cur.getColumnIndex(MediaStore.Images.Media._ID);
			int dPath = cur.getColumnIndex(MediaStore.Images.Media.DATA);
			int date = cur.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
			image_id = cur.getInt(image_idColumn);
			image_path = cur.getString(dPath);
			date_added = cur.getLong(date);
			cur.close();
			if (TextUtils.isEmpty(image_path)) {
				return null;
			} else {
				if (getImageType(image_path)) {
					entity.setContentPath(image_id, image_path, date_added);
					return entity;
				} else {
					return null;
				}

			}
		}
		return null;
	}

	/**
	 * 判断文件类型
	 *
	 * @param path
	 * @return
	 */
	private static boolean getImageType(String path) {
		String prefix = path.substring(path.lastIndexOf(".") + 1);
		return !prefix.equals("gif");
	}

	/**
	 * 获取相册文件夹
	 *
	 * @param context
	 * @return
	 */
	public static List<MediaImgEntity> getGalleryDirs(Activity context) {
		List<MediaImgEntity> galleryList = new ArrayList<>();
		ContentResolver cr = context.getContentResolver();
		String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
				"COUNT(1) AS count"};
		String selection = "0==0) GROUP BY (" + MediaStore.Images.Media.BUCKET_ID;
		String sortOrder = MediaStore.Images.Media.DATE_MODIFIED;
		Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
				selection, null, sortOrder);
		if (cur != null && cur.moveToFirst()) {

			int id_column = cur.getColumnIndex(MediaStore.Images.Media._ID);
			int image_id_column = cur.getColumnIndex(MediaStore.Images.Media.DATA);
			int bucket_id_column = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
			int bucket_name_column = cur
					.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
			int count_column = cur.getColumnIndex("count");
			do {
				// Get the field values
				int id = cur.getInt(id_column);
				String image_path = cur.getString(image_id_column);
				int bucket_id = cur.getInt(bucket_id_column);
				String bucket_name = cur.getString(bucket_name_column);
				int count = cur.getInt(count_column);
				// Do something with the values.
				MediaImgEntity gallery = new MediaImgEntity(id, image_path,
						bucket_id, bucket_name, count);
				galleryList.add(gallery);
			} while (cur.moveToNext());
			cur.close();
		}
		return galleryList;
	}

	public static String[] MediaImgEntityList2Array(Activity context) {
		List<MediaImgEntity> entities = getGalleryEntities(context);
		String[] paths = new String[entities.size()];
		for (int i = 0; i < entities.size(); i++) {
			paths[i] = entities.get(i).img_path;
		}
		return paths;
	}

	/**
	 * 获取所有图片地址
	 *
	 * @param context
	 * @return
	 */
	public static List<MediaImgEntity> getGalleryEntities(Activity context) {
		List<MediaImgEntity> galleryList = new ArrayList<>();
		ContentResolver cr = context.getContentResolver();
		String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED};
		Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
				null, null, MediaStore.Images.Media.DATE_ADDED + " desc");
		if (cur != null && cur.moveToFirst()) {
			int image_id;
			String image_path;
			long date_added;
			int image_idColumn = cur.getColumnIndex(MediaStore.Images.Media._ID);
			int dPath = cur.getColumnIndex(MediaStore.Images.Media.DATA);
			int date = cur.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
			do {
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dPath);
				date_added = cur.getLong(date);
				MediaImgEntity gallery = new MediaImgEntity(image_id,
						image_path, date_added);
				galleryList.add(gallery);
			} while (cur.moveToNext());
			cur.close();
		}
		return galleryList;
	}

}
