package com.hxqc.mall.photolibrary.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.hxqc.mall.photolibrary.model.Album;
import com.hxqc.mall.photolibrary.model.ImageItem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: wanghao
 * Date: 2015-05-12
 * FIXME
 * Todo
 */
public class ImageUtils {

	public static List<ImageItem> sImageItems = new ArrayList<ImageItem>();

	public static ArrayList<Album> getAlbums(Context context) {
		ArrayList<Album> albums = new ArrayList<Album>();


		// which image properties are we querying
		String[] projection = new String[]{
				MediaStore.Images.Media._ID,
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
				MediaStore.Images.Media.BUCKET_ID,
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.DISPLAY_NAME,
				MediaStore.Images.Media.DATE_MODIFIED
		};

		Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		// Make the query.
		Cursor cur = context.getContentResolver().query(images,
				projection, // Which columns to return
				MediaStore.Images.Media.SIZE + ">=? and " + MediaStore.Images.Media.MIME_TYPE + " like 'image%'",        // Which rows to return (all rows)
				new String[]{"9500"},        // Selection arguments (none)
				MediaStore.Images.Media.DATE_MODIFIED            // Ordering
		);

		if (cur != null && cur.moveToFirst()) {
			int id = cur.getColumnIndex(MediaStore.Images.Media._ID);
			int bucketName = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
			int bucketId = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
			int dataIndex = cur.getColumnIndex(MediaStore.Images.Media.DATA);
			int imageName = cur.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
			int imageDate = cur.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);

			Log.d("images", "801--");
			do {
				Log.d("images", "800");
	            /*create a new ImageItem object*/
				ImageItem img = new ImageItem("");
				img.setId(cur.getInt(id));
				img.setTitle(cur.getString(imageName));
//                img.setSubtitle(cur.getString(imageDate));
				img.setPath(cur.getString(dataIndex));
				img.setThumbPath(getThumbPath(context, cur.getInt(id)));

                /*get the bucket id*/
				String bucket_id = cur.getString(bucketId);

                /*check if there is a bucket for this image*/
				boolean ok = false;
				for (int i = 0; i < albums.size(); i++) {
					if (albums.get(i).getId().equals(bucket_id)) {
                        /*found a bucket, add image*/
						albums.get(i).addImage(img);
						ok = true;
						break;
					}
				}

				if (!ok) { /*this image doesn't have a bucket yet*/
					Album alb = new Album();
					alb.setId(bucket_id);
					alb.setName(cur.getString(bucketName));
					alb.addImage(img);
					albums.add(alb);
				}
			} while (cur.moveToNext());
             /*close the cursor*/
			cur.close();
		}


        /*return the albums list*/
		return albums;
	}

	private static String getThumbPath(Context context, int id) {
		Log.d("images", "getThumbPath");
		// which image properties are we querying
		String[] projection = new String[]{
				MediaStore.Images.Thumbnails.DATA
		};

		Uri thumbUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
		// Make the query.
		Cursor cur = context.getContentResolver().query(thumbUri,
				projection, // Which columns to return
				MediaStore.Images.Thumbnails.IMAGE_ID + "=?",         // Which rows to return (all rows)
				new String[]{"" + id},      // Selection arguments (none)
				null         // Ordering
		);
		String path = "";
//        System.out.println("size = "+cur.getCount());
		if (cur != null && cur.moveToFirst()) {
			int data = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
			path = cur.getString(data);
		}
        /*close the cursor*/
		if (cur != null) {
			cur.close();
		}
        
        /*return the albums list*/
		return path;
	}

	public static Bitmap compressAndGetImgBitmap(String path, int mScreenWidth, int mScreenHeight)
			throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inTempStorage = new byte[100 * 1024];
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 8;
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;// 允许可清除
		options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果

		InputStream is = new BufferedInputStream(new FileInputStream(
				new File(path)));

		return BitmapFactory.decodeStream(is, null, options);
	}

	public static Bitmap getBitmapByDecodeFile(String path) {
//1.加载位图
		InputStream is = null;
		try {
			is = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//2.为位图设置100K的缓存
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inTempStorage = new byte[100 * 1024];
//3.设置位图颜色显示优化方式
//ALPHA_8：每个像素占用1byte内存（8位）
//ARGB_4444:每个像素占用2byte内存（16位）
//ARGB_8888:每个像素占用4byte内存（32位）
//RGB_565:每个像素占用2byte内存（16位）
//Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
//4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
		opts.inPurgeable = true;
//5.设置位图缩放比例
//width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；
// 例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，
// 产生的Bitmap大小约为//512*384px。
// 相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
		opts.inSampleSize = 6;
//6.设置解码位图的尺寸信息
		opts.inInputShareable = true;
//7.解码位图
		return BitmapFactory.decodeStream(is, null, opts);
	}

}
