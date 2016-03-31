package org.iii.ideas.catering_service.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.iii.ideas.catering_service.dao.Dish;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.HibernateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/controller")
public class DragDropFileController {

	LinkedList<FileMeta> files = new LinkedList<FileMeta>();
	FileMeta fileMeta = null;

	/***************************************************
	 * URL: /rest/controller/upload upload(): receives files
	 * 
	 * @param request
	 *            : MultipartHttpServletRequest auto passed
	 * @param response
	 *            : HttpServletResponse auto passed
	 * @return LinkedList<FileMeta> as json format
	 ****************************************************/
	@RequestMapping(value = "/Upload", method = RequestMethod.POST)
	public @ResponseBody
	LinkedList<FileMeta> upload(MultipartHttpServletRequest request, HttpServletResponse response) {

		String userType = "";
		String userName = "";
		HttpSession httpsession = request.getSession();
		Integer kitchenId = null;
		String userkitchenId = (String) httpsession.getAttribute("account");
		kitchenId = Integer.valueOf(userkitchenId);

		SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		java.util.Date date = new java.util.Date();

		try {

			// 1. build an iterator
			Iterator<String> itr = request.getFileNames();
			MultipartFile mpf = null;

			// 2. get each file
			while (itr.hasNext()) {

				// 2.1 get next MultipartFile
				mpf = request.getFile(itr.next());
				System.out.println(mpf.getOriginalFilename() + " uploaded! " + files.size());

				// 2.2 if files > 10 remove the first from the list
				if (files.size() >= 10)
					files.pop();

				// 2.3 create new fileMeta
				fileMeta = new FileMeta();
				fileMeta.setFileName(mpf.getOriginalFilename());
				fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
				fileMeta.setFileType(mpf.getContentType());
				fileMeta.setFileDesc("");

				String dishName = FilenameUtils.removeExtension(mpf.getOriginalFilename()).trim();
				String ext = FilenameUtils.getExtension(mpf.getOriginalFilename());

				if ((ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("png") || ext.toLowerCase().equals(
						"gif")) == false) {
					// throw new Exception("確認檔案格式為 jpg/png/gif 目前格式:"+ext);
					fileMeta.setFileDesc("確認檔案格式為 jpg/png/gif 目前格式:" + ext);
				} else {
					Dish dish = HibernateUtil.queryDishByName(session, kitchenId, dishName);
					if (dish == null) {
						fileMeta.setFileDesc("菜色檔中找不到此菜色名稱:"+dishName);
					} else {
						try {
							fileMeta.setBytes(mpf.getBytes());
							// copy file to local disk (make sure the path "e.g. D:/temp/files" exists)
							String imgFullPath = CateringServiceUtil.getDishImageFileName(kitchenId, dish.getDishId(), ext);
							FileCopyUtils.copy(mpf.getBytes(),
									new FileOutputStream(imgFullPath));
							fileMeta.setFileDesc("上傳完成!"+imgFullPath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				// 2.4 add to files
				files.add(fileMeta);

			}
			if (!tx.wasCommitted()) {
				tx.commit();
			}
			session.close();
		} catch (Exception e) {
			session.close();
			e.printStackTrace();
			return files;
		}
		// result will be like this
		// [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
		return files;

	}

	/***************************************************
	 * URL: /rest/controller/get/{value} get(): get file as an attachment
	 * 
	 * @param response
	 *            : passed by the server
	 * @param value
	 *            : value from the URL
	 * @return void
	 ****************************************************/
	@RequestMapping(value = "/get/{value}", method = RequestMethod.GET)
	public void get(HttpServletResponse response, @PathVariable String value) {
		FileMeta getFile = files.get(Integer.parseInt(value));
		try {
			response.setContentType(getFile.getFileType());
			response.setHeader("Content-disposition", "attachment; filename=\"" + getFile.getFileName() + "\"");
			FileCopyUtils.copy(getFile.getBytes(), response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
