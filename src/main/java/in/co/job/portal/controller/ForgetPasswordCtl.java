package in.co.job.portal.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.job.portal.bean.BaseBean;
import in.co.job.portal.bean.UserBean;
import in.co.job.portal.exception.ApplicationException;
import in.co.job.portal.exception.RecordNotFoundException;
import in.co.job.portal.model.UserModel;
import in.co.job.portal.util.DataUtility;
import in.co.job.portal.util.DataValidator;
import in.co.job.portal.util.PropertyReader;
import in.co.job.portal.util.ServletUtility;



/**
 * Servlet implementation class ForgetPasswordCtl
 */
/**
 * ForgetPassword functionality Controller. Performs operation for Authentication, 
 * and Email send to Get Password
 *
 * @author Navigable Set
 * @version 1.0
 * @Copyright (c) Navigable Set
 * 
 */
@WebServlet(name = "ForgetPasswordCtl", urlPatterns = { "/ForgetPasswordCtl" })
public class ForgetPasswordCtl extends BaseCtl {
private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(ForgetPasswordCtl.class);
       
	/**
	 * Validate input Data Entered By User
	 * 
	 * @param request
	 * @return
	 */

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("ForgetPasswordCtl validate  Method Started");

        boolean pass = true;

        String login = request.getParameter("login");

        if (in.co.job.portal.util.DataValidator.isNull(login)) {
            request.setAttribute("login",
                    PropertyReader.getValue("error.require", "Email Id"));
            pass = false;
        } else if (!DataValidator.isEmail(login)) {
            request.setAttribute("login",
                    PropertyReader.getValue("error.email", "Email Id"));
            pass = false;
        }
        log.debug("ForgetPasswordCtl  validate Method Ended");

        return pass;
	}

	
	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		 log.debug("ForgetPasswordCtl Method populatebean Started");

	        UserBean bean = new UserBean();

	        bean.setLogin(DataUtility.getString(request.getParameter("login")));

	        log.debug("ForgetPasswordCtl Method populatebean Ended");

	        return bean;
	}

		
	/**
	 * Contains display logic
	 */
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("ForgetPasswordCtl Method doGet Started");

        ServletUtility.forward(getView(), request, response);
	}
	/**
	 * Contains submit logic
	 */
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("ForgetPasswordCtl Method doPost Started");

        String op = DataUtility.getString(request.getParameter("operation"));

        UserBean bean = (UserBean) populateBean(request);

        // get model
        UserModel model = new UserModel();

        if (OP_GO.equalsIgnoreCase(op)) {

            try {
                model.forgetPassword(bean.getLogin());
                
                ServletUtility.setSuccessMessage(
                        "Password has been sent to your email id.", request);
            } catch (RecordNotFoundException e) {
                ServletUtility.setErrorMessage(e.getMessage(), request);
                log.error(e);
            } catch (ApplicationException e) {
                log.error(e);
                ServletUtility.handleException(e, request, response);
                return;
               
            } 
            ServletUtility.forward(getView(), request, response);
        }

        log.debug("ForgetPasswordCtl Method doPost Ended");
	}
	@Override
	protected String getView() {
		 return JPView.FORGET_PASSWORD_VIEW;
	}

}
