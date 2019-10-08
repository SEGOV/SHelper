<%@ page import="
	com.netcracker.jsp.*,
	com.netcracker.ejb.framework.HomeInterfaceHelper,
	com.netcracker.jsp.html.*,
	com.netcracker.jsp.xml.*,
	java.sql.Connection,
	java.sql.PreparedStatement,
	java.sql.ResultSet,
	java.sql.SQLException,
	java.sql.Statement,
	java.util.List,
	java.math.BigInteger,
	java.sql.ResultSetMetaData,
	java.util.ArrayList,
	java.util.HashMap,
	org.jdom.Element,
	com.netcracker.ejb.attribute.Attribute,
	org.apache.commons.lang.StringUtils,
	com.netcracker.ejb.framework.UnicodeURLEncoder,
	com.netcracker.jsp.ItemListWriter,
	com.netcracker.platform.toolset.ExceptionTool,
	java.io.IOException,
	com.netcracker.platform.toolset.CommonTool,
	javax.servlet.ServletException,
	javax.servlet.http.HttpServlet,
	javax.servlet.http.HttpServletRequest,
	javax.servlet.http.HttpServletResponse
"%>
<%

	class RBMExecutorSheet extends UniSheet {
		public List<String> processes = new ArrayList();			
		public List<String> p_numbers = new ArrayList();
		public List<String> get_event_files = new ArrayList();
		public String p_number = "";
		public String get_event_file = "";
		public String event_file = "";
		public String control_file = "";		
		public String phone_number = "";
		public String process="";
		public String mode="";
		private String title = "RBM Executor";
		public String BANum = "";
		public String RBMUser = "";
		public String RBMPassword = "";
		
		public String getName() {
             return title;
        }
			
		@Override
        public void init() throws Exception
        {   
			processes.add("BG");
			processes.add("RATE");			
			processes.add("Printed Bill");
			processes.add("Cash Payment");
			p_numbers.add("Get by Query");
			p_numbers.add("Enter manually");
			get_event_files.add("Generate");
			get_event_files.add("Specify path");
        	super.init();
        }
		
		@Override
        public void parseParameters()
        {   
			mode = getValidParameter("mode");
        }
		
		public void printError(String errorMessage, Throwable e) throws Exception {
			out.println("<div onClick=\"toggle_show('errormessage')\"><span style=\"text-decoration:underline;\"><b><font color=\"red\">"+errorMessage+"</font></b></span></div>");
			out.println("<div id=\"errormessage\" style=\"display: none\">");
			out.println("<b>" + ExceptionTool.getMessage(e) + "</b><br/>");
			out.println("<b>" + ExceptionTool.getStackTrace(e) + "</b><br/>");
			printStackTrace(e);
   			out.println("</div>");
        }
		
		private void printStackTrace(Throwable e) throws Exception {
            out.print("<br/><font color='black'>");
            out.print(e.getClass().getName() + ": <br/>");
            StackTraceElement stack[] = e.getStackTrace();
            for (StackTraceElement aStack : stack) {
                out.print("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + aStack.toString() + "<br/>");
            }
            out.print("</font><br/>");
            if (e.getCause() != null) {
                out.print("<br/>");
                printStackTrace(e.getCause());
            }
        }
		
		public void printWindowContent() throws Exception {
            try{
				printScript();
				printMain();
			} catch (Throwable e) {
				printError(e.toString() , e);	
			}
        }
		
		public void printScript() throws Exception {
			out.println("<script>function toggle_show(id){");
			out.println("var e = document.getElementById(id);");
			out.println("if(e.style.display == 'none'){");
			out.println("e.style.display = 'block';");
			out.println("} else {");
			out.println("e.style.display = 'none';");
			out.println("}");
			out.println("}</script>");		
			out.println("<script>function show_number_and_path(id_phone, id_path, id_c_path){");
			out.println("var ph = document.getElementById(id_phone);");
			out.println("var pa = document.getElementById(id_path);");
			out.println("var pc = document.getElementById(id_c_path);");
			out.println("var e = document.getElementById('get_event_file');");
			out.println("if(e.options[e.selectedIndex].value == \"Generate\"){");
			out.println("ph.style.display = 'block';");
			out.println("pa.style.display = 'none';");
			out.println("pc.style.display = 'none';");
			out.println("} else {");
			out.println("ph.style.display = 'none';");
			out.println("pa.style.display = 'block';");
			out.println("pc.style.display = 'block';");
			out.println("}");
			out.println("}</script>");
			out.println("<script>function show_phone_number(id){");
			out.println("var p = document.getElementById(id);");
			out.println("var e = document.getElementById('p_number');");
			out.println("if(e.options[e.selectedIndex].value == \"Enter manually\"){");
			out.println("p.style.display = 'block';");
			out.println("} else {");
			out.println("p.style.display = 'none';");
			out.println("}");
			out.println("}</script>");
			out.println("<script>function show_event_file(id){");
			out.println("var p = document.getElementById(id);");
			out.println("var e = document.getElementById('process');");
			out.println("if(e.options[e.selectedIndex].value == \"RATE\"){");
			out.println("p.style.display = 'block';");
			out.println("} else {");
			out.println("p.style.display = 'none';");
			out.println("}");
			out.println("}</script>");
		}
		
		public void printMain() throws Exception {
			BANum = getParameter("BANum");
			getPageBase().getResponse().addCookie(new Cookie("BANum", BANum));
			process = getParameter("process")==null?"BG":getParameter("process");
			getPageBase().getResponse().addCookie(new Cookie("process", process));
			RBMUser = getParameter("RBMUser")==null?"rbmvm":getParameter("RBMUser");
			getPageBase().getResponse().addCookie(new Cookie("RBMUser", RBMUser));
			RBMPassword = getParameter("RBMPassword")==null?"rbmvm":getParameter("RBMPassword");
			getPageBase().getResponse().addCookie(new Cookie("RBMPassword", RBMPassword));
			p_number = getParameter("p_number")==null?"Get by Query":getParameter("p_number");
			getPageBase().getResponse().addCookie(new Cookie("p_number", p_number));
			phone_number = getParameter("phone_number")==null?"":getParameter("phone_number");
			getPageBase().getResponse().addCookie(new Cookie("phone_number", phone_number));
			event_file = getParameter("event_file")==null?"":getParameter("event_file");
			getPageBase().getResponse().addCookie(new Cookie("event_file", event_file));
			control_file = getParameter("control_file")==null?"":getParameter("control_file");
			getPageBase().getResponse().addCookie(new Cookie("control_file", control_file));
			
			out.println("<table style=\"border-collapse:separate; border-spacing:0 15px; margin-top:-15px;\">");
			out.println("<form>");
			out.println("<tr><td><pre>BA Number     </pre><input type='text' name='BANum' value='" + (!StringUtils.isEmpty(BANum) ? BANum : "") + "'></td></tr>");
			
			String selected = "";	
			out.println("<tr><td><pre>What to run   </pre><select id='process' name='process' onchange=\"show_event_file('event_file')\">");
			for (String p:processes){
				selected = (p.equals(process)) ? "selected ":"";
				out.println("<option " + selected + "value=\"" + p + "\">" + p + "</option>");					 
			}
			out.println("</select></td></tr>");
			
			out.println("<tr><td><pre>RBM user      </pre><input type='text' name='RBMUser' value='" + (!StringUtils.isEmpty(RBMUser) ? RBMUser : "") + "'></td></tr>");
			out.println("<tr><td><pre>RBM password  </pre><input type='text' name='RBMPassword' value='" + (!StringUtils.isEmpty(RBMPassword) ? RBMPassword : "") + "'></td></tr>");
			
			String display = "";
			if (!StringUtils.isEmpty(process)){
				display = process.equals("RATE")?"block":"none";
			} else {
				display = "none";
			}
			
			out.println("<tr id=\"event_file\" style=\"display: "+display+"\"><td>");
			out.println("<pre>How to get event file</pre><select id='get_event_file' name='get_event_file' onchange=\"show_number_and_path('number','pth','c_pth')\">");
			for (String p:get_event_files){
				selected = (p.equals(get_event_file)) ? "selected ":"";
				out.println("<option " + selected + "value=\"" + p + "\">" + p + "</option>");					 
			}		
			out.println("</select>");			
			out.println("</td>");			

			display = get_event_file.equals("Specify path")?"block":"none";		
			out.println("<td id=\"pth\" style=\"display: "+display+"\"><div>Enter Eventfile path on server</div><input type='text' name='event_file' value='" + (!StringUtils.isEmpty(event_file) ? event_file : "") + "'></td>");
			out.println("<td id=\"c_pth\" style=\"display: "+display+"\"><div>Enter Controlfile path on server</div><input type='text' name='control_file' value='" + (!StringUtils.isEmpty(control_file) ? control_file : "") + "'></td>");
			
			display = get_event_file.equals("Generate")?"block":"none";		
			out.println("<td id=\"number\" style=\"display: "+display+"\">");
			out.println("<pre>Phone number  </pre><select name='p_number' id= 'p_number' onchange=\"show_phone_number('phone_number')\">");
			for (String p:p_numbers){
				selected = (p.equals(p_number)) ? "selected ":"";
				out.println("<option " + selected + "value=\"" + p + "\">" + p + "</option>");					 
			}						
			out.println("</select>");
			display = p_number.equals("Get by Query")?"none":"block";					
			out.println("<div id=\"phone_number\" style=\"display: "+display+"\"><pre>Enter Phone number (including 32)</pre><input type='text' name='phone_number' value='" + (!StringUtils.isEmpty(phone_number) ? phone_number : "") + "'></div>");			
			out.println("</td>");
			out.println("</tr>");			
			
			out.println("<tr><td><button>Run Process</button></td></tr>");	
			
			boolean isPhoneOK = true;
			if (!StringUtils.isEmpty(BANum)){
				if (isExist(BANum)){
					List<String> exec_result = null;
					List<HashMap<String, String>> results_date = getResults("select to_char(next_bill_dtm, 'yyyyMMdd')||' 23300000' as ddd from account@RBM_dblink where account_num = '"+BANum+"'");
					String next_bill_date = results_date.get(0).get("DDD");
					List<HashMap<String, String>> results_domain = getResults("select DOMAIN_GROUP_ID from domaingroup@rbm_dblink d, account@rbm_dblink a where a.account_num = '"+BANum+"' and a.domain_id between d.START_DOMAIN_ID and d.END_DOMAIN_ID");
					String domain_id = results_domain.get(0).get("DOMAIN_GROUP_ID");
					if (process.equals("BG")){
						runPrepSql();						
						exec_result = new ArrayList<String>(exec_bg_cmd(next_bill_date, domain_id, BANum));	
					} else if (process.equals("Printed Bill")){
 						runPrepSql();
						runPrepPrintSql();
						exec_result = new ArrayList<String>(exec_printed_bg_cmd(next_bill_date, domain_id, BANum));
					} else if (process.equals("RATE")){
						runPrepSql();
						if(get_event_file.equals("Specify path")){
							exec_result = new ArrayList<String>(exec_rate_from_file(event_file, control_file, next_bill_date, domain_id, BANum));
						} else {
							List<HashMap<String, String>> results_event_time = getResults("select to_char(next_bill_dtm - 25/24, 'yyyy/MM/dd-hh24-mm-ss')||'.00' as ddd from account@RBM_dblink where account_num = '"+BANum+"'");
							String event_time = results_event_time.get(0).get("DDD");
							if (p_number.equals("Get by Query")){
								List<HashMap<String, String>> results_phone = getResults("select EVENT_SOURCE as DDD from CustEventSource@rbm_dblink c join account@rbm_dblink a on a.customer_ref = c.customer_ref  and a.account_num='" + BANum + "'");
								if (results_phone.size() == 0){
									out.println("<tr><td>");
									out.println("Unable to find active Phone Number. Please verify that product is activated");
									out.println("</td></tr>");
									isPhoneOK = false;
								} else {
									String phone_number_sql = results_phone.get(0).get("DDD");
									prepare_event_files(phone_number_sql, event_time);															
									exec_result = new ArrayList<String>(exec_rate_cmd(next_bill_date, domain_id, BANum));
								}
							} else if (p_number.equals("Enter manually")){
								List<HashMap<String, String>> results_phone32 = getResults("select '32'||SUBSTR(EVENT_SOURCE,2) as ddd from CustEventSource@rbm_dblink c join account@rbm_dblink a on a.customer_ref = c.customer_ref  and a.account_num='" + BANum + "' and '32'||SUBSTR(EVENT_SOURCE,2) in ('"+phone_number+"')");
								List<HashMap<String, String>> results_phone = getResults("select EVENT_SOURCE as DDD from CustEventSource@rbm_dblink c join account@rbm_dblink a on a.customer_ref = c.customer_ref  and a.account_num='" + BANum + "' and EVENT_SOURCE in ('"+phone_number+"')");
								if (results_phone32.size() == 0 && results_phone.size() == 0){
									out.println("<tr><td>");
									out.println("Unable to find this Phone Number. Please verify that product is activated or check entered number");
									out.println("</td></tr>");
									isPhoneOK = false;
								} else {								
									prepare_event_files(phone_number, event_time);															
									exec_result = new ArrayList<String>(exec_rate_cmd(next_bill_date, domain_id, BANum));
								}
							}
						}
					} else if (process.equals("Cash Payment")){
						runPrepSql();
						List<HashMap<String, String>> results_payment_date = getResults("select to_char(CURRENT_DATE, 'yyyy-MM-dd') as ddd from dual");
						String payment_date = results_payment_date.get(0).get("DDD");
						List<HashMap<String, String>> results_payment_amount = getResults("select to_char(balance_out_mny/100000000, '999999999D999') as ddd from BillSummary@rbm_dblink where account_num = '"+BANum+"'");
						String payment_amount = results_payment_amount.get(0).get("DDD");
						List<HashMap<String, String>> results_customer_name = getResults("select first_name||' '||last_name as ddd from Contact@rbm_dblink where customer_ref = (select customer_ref from account@rbm_dblink where account_num = '"+BANum+"')");
						String customer_name = results_customer_name.get(0).get("DDD");
						List<HashMap<String, String>> results_customer_location = getResults("select address_2, zipcode, address_1 from Address@rbm_dblink where customer_ref = (select customer_ref from account@rbm_dblink where account_num = '"+BANum+"')");
						String customer_address_2 = results_customer_location.get(0).get("ADDRESS_2");
						String customer_address_1 = results_customer_location.get(0).get("ADDRESS_1");
						String customer_zipcode = results_customer_location.get(0).get("ZIPCODE");
						List<HashMap<String, String>> results_customer_iban = getResults("select nvl(iban, 'BE87734309108794') as ddd from AccountAttributes@rbm_dblink where account_num = '"+BANum+"'");
						String customer_iban = results_customer_iban.get(0).get("DDD");
						List<HashMap<String, String>> results_customer_bank = getResults("select nvl(bank_code, 'KREDBEBB') from prmandate@rbm_dblink where account_num = '"+BANum+"' union select 'KREDBEBB' from dual where NOT EXISTS (select bank_code from prmandate@rbm_dblink where account_num = '"+BANum+"')");
						String customer_bank = results_customer_bank.get(0).get("DDD");
						List<HashMap<String, String>> results_payment_ref = getResults("select substr(a,1,3) || '/' || substr(a,4,4)  || '/' || substr(a,8) as ddd from (select bill_label a from BillSummary@rbm_dblink where account_num = '"+BANum+"')");
						String payment_ref = results_payment_ref.get(0).get("DDD");
						List<HashMap<String, String>> results_current_dtm = getResults("select to_char(CURRENT_DATE, 'yyyy_MM_dd_HH24_MI') as ddd from dual");
						String current_dtm = results_current_dtm.get(0).get("DDD");
						prepare_payment_file(current_dtm, payment_date, payment_amount, customer_name, customer_address_1, customer_address_2, customer_zipcode, customer_iban, customer_bank, payment_ref);
						List<HashMap<String, String>> results_event_time = getResults("select to_char(actual_bill_dtm + 5, 'yyyyMMdd')||' 23000000' as ddd from BillSummary@RBM_dblink where account_num = '"+BANum+"'");
						String event_time = results_event_time.get(0).get("DDD");
						exec_result = new ArrayList<String>(exec_payment_cmd(event_time, domain_id, BANum));	
					}
					if (isPhoneOK){
						String result = exec_result.get(exec_result.size() - 1);
						String color = (result.equals("SUCCESS"))?"green":"red";
						if (result.equals("UNKNOWN")) {
							color = "grey";
						}
						out.println("<tr><td>");
						out.println("<div onClick=\"toggle_show('logs')\"><span style=\"text-decoration:underline;\"><b><font color=\"" + color + "\" size = \"7\">"+result+"</font></b></span></div>");
						out.println("<div id=\"logs\" style=\"display: none\">");					
						out.println("<b>Next bill date:"+next_bill_date+"</b><br>");
						out.println("<b>Domain ID:"+domain_id+"</b>");
						for (String log_record:exec_result){
							out.println("<b>" + log_record + "</b><br>");
						}
						out.println("</div>");
						out.println("</td></tr>");
					}
				} else {
					out.println("<tr><td>Entered BA does not exist</td></tr>");					
				}
			}		
			out.println("</table>");
			out.println("</form>");
		}
		
		public List<String> exec_payment_cmd(String event_time, String domain_id, String BANum) throws Exception {
			boolean res = false;
			
			List<HashMap<String, String>> results_events = getResults("select count(account_num) as ddd from ACCOUNTPAYMENT@RBM_dblink where account_num='"+BANum+"'");
			String events_before = results_events.get(0).get("DDD");			
			
			List<String> logs = new ArrayList<String>();
			ProcessBuilder builder = new ProcessBuilder( "/bin/bash" );
			Process p=null;
			p = builder.start();
			BufferedWriter p_stdin = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));						
			execute_cmd(p_stdin, "su - " + RBMUser);
			execute_cmd(p_stdin, RBMPassword);			
			execute_cmd(p_stdin, "cd /u02/netcracker/rbm/" + RBMUser + "/infinys_root");
			execute_cmd(p_stdin, ". ./infinys.env");			
			execute_cmd(p_stdin, "cd /u02/netcracker/rbm/" + RBMUser + "/infinys_root/RB/bin");
			execute_cmd(p_stdin, "./TLN-PAYMENT.pl -a '-mode payment' > payment.log 2>&1");
			execute_cmd(p_stdin, "cat payment.log");
			execute_cmd(p_stdin, "cd /u02/netcracker/rbm/" + RBMUser + "/infinys_root");
			execute_cmd(p_stdin, "export GENEVA_FIXEDDATETIME='" + event_time + "'");
			execute_cmd(p_stdin, "DEBTage -dg 1 -a \"-allocateOverpay\"");
			execute_cmd(p_stdin, "exit");
			execute_cmd(p_stdin, "exit");
			
			String s="";
			while ((s = br.readLine()) != null){
				logs.add(s);
				if (s.contains("No of records in")){
					res = true;
				}
			}
			
			results_events = getResults("select count(account_num) as ddd from ACCOUNTPAYMENT@RBM_dblink where account_num='"+BANum+"'");
			String events_after = results_events.get(0).get("DDD");
			String result = (Integer.parseInt(events_after) > Integer.parseInt(events_before))?"SUCCESS":"FAILURE";
			logs.add(result);
			p_stdin.close();
			br.close();
			return logs;
		}
		
		public List<String> prepare_payment_file(String current_dtm, String payment_date, String payment_amount, String customer_name, String customer_address_1, String customer_address_2, String customer_zipcode, String customer_iban, String customer_bank, String payment_ref) throws Exception {
			List<String> logs = new ArrayList<String>();
			ProcessBuilder builder = new ProcessBuilder( "/bin/bash" );
			Process p=null;
			p = builder.start();
			BufferedWriter p_stdin = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String payment_file = "cash_payment"+ current_dtm + ".xml";
			String random_ElctrncSeqNb = Integer.toString(100 + (int)(Math.random() * ((999 - 100))));
			
			execute_cmd(p_stdin, "su - " + RBMUser);
			execute_cmd(p_stdin, RBMPassword);			
			execute_cmd(p_stdin, "cd /u02/netcracker/rbm/" + RBMUser + "/infinys_root/");
			execute_cmd(p_stdin, "if [ ! -d \"custom_root/CUSTOM/PAYCREATE/INPUT\" ]; then mkdir -p custom_root/CUSTOM/PAYCREATE/INPUT; fi");
			execute_cmd(p_stdin, "cd custom_root/CUSTOM/PAYCREATE/INPUT");
			execute_cmd(p_stdin, "if [ ! -e \""+payment_file+"\" ]; then touch "+payment_file+"; chmod 777 "+payment_file+";fi");
						
			execute_cmd(p_stdin, "echo \\<?xml version=\\\"1.0\\\"   encoding=\\\"UTF-8\\\"?\\> > " + payment_file);				
			execute_cmd(p_stdin, "echo \\<Document xmlns=\\\"urn:iso:std:iso:20022:tech:xsd:camt.053.001.02\\\"\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<BkToCstmrStmt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<GrpHdr\\> >> " + payment_file);
			int rand_num = (int)(Math.random()*(9999-1000) + 1000);
			execute_cmd(p_stdin, "echo \\<MsgId\\>0018" + Integer.toString(rand_num) + "/20185603T004847.765490+01H\\</MsgId\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CreDtTm\\>2018-02-27T00:00:00\\</CreDtTm\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<MsgRcpt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Nm\\>TELENET BVBA\\</Nm\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</MsgRcpt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</GrpHdr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Stmt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Id\\>405050461148EUR/171028T004853030\\</Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<ElctrncSeqNb\\>" + random_ElctrncSeqNb + "\\</ElctrncSeqNb\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CreDtTm\\>2018-02-27T00:00:00\\</CreDtTm\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Acct\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<IBAN\\>BE11405050461148\\</IBAN\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Tp\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Prtry\\>KBC-Bedrijfsrekening\\</Prtry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Tp\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Ccy\\>EUR\\</Ccy\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Ownr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Nm\\>TELENET BVBA\\</Nm\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<OrgId\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Othr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Id\\>0473416418\\</Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Othr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</OrgId\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Ownr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Svcr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<FinInstnId\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<BICFI\\>KREDBEBB\\</BICFI\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</FinInstnId\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Svcr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Acct\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Bal\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Tp\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CdOrPrtry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Cd\\>OPBD\\</Cd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</CdOrPrtry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Tp\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Amt Ccy=\\\"EUR\\\"\\>0.000\\</Amt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CdtDbtInd\\>CRDT\\</CdtDbtInd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Dt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Dt\\>2018-02-26\\</Dt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Dt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Bal\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Bal\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Tp\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CdOrPrtry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Cd\\>CLBD\\</Cd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</CdOrPrtry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Tp\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Amt Ccy=\\\"EUR\\\"\\>0.000\\</Amt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CdtDbtInd\\>CRDT\\</CdtDbtInd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Dt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Dt\\>2018-02-27\\</Dt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Dt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Bal\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Bal\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Tp\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CdOrPrtry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Cd\\>CLAV\\</Cd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</CdOrPrtry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Tp\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Amt Ccy=\\\"EUR\\\"\\>0.000\\</Amt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CdtDbtInd\\>CRDT\\</CdtDbtInd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Dt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Dt\\>2018-02-27\\</Dt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Dt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Bal\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<TxsSummry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<TtlNtries\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<NbOfNtries\\>1\\</NbOfNtries\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Sum\\>" + payment_amount + "\\</Sum\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<TtlNetNtryAmt\\>0.000\\</TtlNetNtryAmt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CdtDbtInd\\>CRDT\\</CdtDbtInd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</TtlNtries\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<TtlCdtNtries\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<NbOfNtries\\>1\\</NbOfNtries\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Sum\\>" + payment_amount + "\\</Sum\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</TtlCdtNtries\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<TtlDbtNtries\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<NbOfNtries\\>0\\</NbOfNtries\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Sum\\>0.000\\</Sum\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</TtlDbtNtries\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</TxsSummry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Ntry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Amt Ccy=\\\"EUR\\\"\\>" + payment_amount + "\\</Amt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CdtDbtInd\\>CRDT\\</CdtDbtInd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Sts\\>BOOK\\</Sts\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<BookgDt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Dt\\>" + payment_date + "\\</Dt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</BookgDt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<ValDt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Dt\\>2018-02-26\\</Dt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</ValDt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<AcctSvcrRef\\>SCTOFBIONLO000001405050461148EUR085\\</AcctSvcrRef\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<BkTxCd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Domn\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Cd\\>PMNT\\</Cd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Fmly\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Cd\\>RCDT\\</Cd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<SubFmlyCd\\>DMCT\\</SubFmlyCd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Fmly\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Domn\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Prtry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Cd\\>0150000\\</Cd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Issr\\>BBA\\</Issr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Prtry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</BkTxCd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<NtryDtls\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<TxDtls\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Refs\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Prtry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Tp\\>LegalSequenceNumber\\</Tp\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Ref\\>207\\</Ref\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Prtry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Refs\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Amt Ccy=\\\"EUR\\\"\\>" + payment_amount + "\\</Amt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CdtDbtInd\\>CRDT\\</CdtDbtInd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<RltdPties\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Dbtr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Nm\\>" + customer_name + "\\</Nm\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<PstlAdr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<AdrLine\\>" + customer_address_2 + "\\</AdrLine\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<AdrLine\\>" + customer_zipcode + " " + customer_address_1 + "\\</AdrLine\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</PstlAdr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Dbtr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<DbtrAcct\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<IBAN\\>" + customer_iban + "\\</IBAN\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</DbtrAcct\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Cdtr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Nm\\>TELENET BVBA\\</Nm\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<OrgId\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Othr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Id\\>0473416418\\</Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Issr\\>KBO-BCE\\</Issr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Othr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</OrgId\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Cdtr\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CdtrAcct\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<IBAN\\>BE11405050461148\\</IBAN\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Id\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Ccy\\>EUR\\</Ccy\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</CdtrAcct\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</RltdPties\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<RltdAgts\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<DbtrAgt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<FinInstnId\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<BICFI\\>" + customer_bank + "\\</BICFI\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</FinInstnId\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</DbtrAgt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<CdtrAgt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<FinInstnId\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<BICFI\\>KREDBEBB\\</BICFI\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</FinInstnId\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</CdtrAgt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</RltdAgts\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<RmtInf\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\<Ustrd\\>+++" + payment_ref + "+++\\</Ustrd\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</RmtInf\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</TxDtls\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</NtryDtls\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Ntry\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Stmt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</BkToCstmrStmt\\> >> " + payment_file);
			execute_cmd(p_stdin, "echo \\</Document\\> >> " + payment_file);			
			
			execute_cmd(p_stdin, "exit");
			execute_cmd(p_stdin, "exit");
			
			String s="";
			while ((s = br.readLine()) != null){
				logs.add(s);				
			}
			String result ="SUCCESS";
			logs.add(result);
			p_stdin.close();
			br.close();
			return logs;
		}
		
		public List<String> exec_rate_cmd(String next_bill_date, String domain_id, String BANum) throws Exception {
			boolean res = false;
			List<String> logs = new ArrayList<String>();
			ProcessBuilder builder = new ProcessBuilder( "/bin/bash" );
			Process p=null;
			p = builder.start();
			BufferedWriter p_stdin = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));						
			execute_cmd(p_stdin, "su - " + RBMUser);
			execute_cmd(p_stdin, RBMPassword);			
			execute_cmd(p_stdin, "cd /u02/netcracker/rbm/" + RBMUser + "/infinys_root/");
			execute_cmd(p_stdin, ". ./infinys.env");
			execute_cmd(p_stdin, "export GENEVA_FIXEDDATETIME='" + next_bill_date + "'");
			execute_cmd(p_stdin, "FID -a '-sourceDir \"/u02/netcracker/rbm/" + RBMUser + "/infinys_root/AT_dir\"'");
			execute_cmd(p_stdin, "EFD");
			execute_cmd(p_stdin, "RATE -dg " + domain_id + " -a \"-plugInPath '/u02/netcracker/rbm/" + RBMUser + "/infinys_root/RB/lib/RMFP.so' -commitSize 1000 -bufferSize 2000\"");
			execute_cmd(p_stdin, "exit");
			execute_cmd(p_stdin, "exit");
			
			String s="";
			while ((s = br.readLine()) != null){
				logs.add(s);
			}
			
			List<HashMap<String, String>> results_events = getResults("select count(account_num) as ddd from costedevent@RBM_dblink where account_num='"+BANum+"'");
			String events = results_events.get(0).get("DDD");
			String result = (Integer.parseInt(events) > 0)?"SUCCESS":"FAILURE";
			logs.add(result);
			p_stdin.close();
			br.close();
			return logs;
		}
		
		public List<String> exec_rate_from_file(String event_file, String control_file, String next_bill_date, String domain_id, String BANum) throws Exception {
			boolean res = false;
			List<String> logs = new ArrayList<String>();
			ProcessBuilder builder = new ProcessBuilder( "/bin/bash" );
			Process p=null;
			p = builder.start();
			BufferedWriter p_stdin = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));						
			execute_cmd(p_stdin, "su - " + RBMUser);
			execute_cmd(p_stdin, RBMPassword);
			execute_cmd(p_stdin, "cd /u02/netcracker/rbm/" + RBMUser + "/infinys_root/");
			execute_cmd(p_stdin, "if [ ! -d \"AT_dir\" ]; then mkdir AT_dir; chmod 777 AT_dir;fi");
			execute_cmd(p_stdin, "cd AT_dir");			
			execute_cmd(p_stdin, "cp " + event_file + " .");
			execute_cmd(p_stdin, "cp " + control_file + " .");
			execute_cmd(p_stdin, "cd /u02/netcracker/rbm/" + RBMUser + "/infinys_root/");
			execute_cmd(p_stdin, ". ./infinys.env");
			execute_cmd(p_stdin, "export GENEVA_FIXEDDATETIME='" + next_bill_date + "'");
			execute_cmd(p_stdin, "FID -a '-sourceDir \"/u02/netcracker/rbm/" + RBMUser + "/infinys_root/AT_dir\"'");
			execute_cmd(p_stdin, "EFD");
			execute_cmd(p_stdin, "RATE -dg " + domain_id + " -a \"-plugInPath '/u02/netcracker/rbm/" + RBMUser + "/infinys_root/RB/lib/RMFP.so' -commitSize 1000 -bufferSize 2000\"");
			execute_cmd(p_stdin, "exit");
			execute_cmd(p_stdin, "exit");
			
			String s="";
			while ((s = br.readLine()) != null){
				logs.add(s);
			}
			
			List<HashMap<String, String>> results_events = getResults("select count(account_num) as ddd from costedevent@RBM_dblink where account_num='"+BANum+"'");
			String events = results_events.get(0).get("DDD");
			String result = (Integer.parseInt(events) > 0)?"SUCCESS":"FAILURE";
			logs.add(result);
			p_stdin.close();
			br.close();
			return logs;
		}
		
		public List<String> prepare_event_files(String phone_num, String event_time) throws Exception {
			List<String> logs = new ArrayList<String>();
			ProcessBuilder builder = new ProcessBuilder( "/bin/bash" );
			Process p=null;
			p = builder.start();
			BufferedWriter p_stdin = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String event_file = "event_file_sub_type_1.tlnt.im.darwin.sndbx.s41.novalocal_1910.1";
			String control_event_file = "control_GENERATED_EVENTS_112.tlnt.im.darwin.sndbx.s41.novalocal_17801.20170618194439";
			execute_cmd(p_stdin, "su - " + RBMUser);
			execute_cmd(p_stdin, RBMPassword);			
			execute_cmd(p_stdin, "cd /u02/netcracker/rbm/" + RBMUser + "/infinys_root/");
			execute_cmd(p_stdin, "if [ ! -d \"AT_dir\" ]; then mkdir AT_dir; chmod 777 AT_dir;fi");
			execute_cmd(p_stdin, "cd AT_dir");
			execute_cmd(p_stdin, "if [ ! -e \""+event_file+"\" ]; then touch "+event_file+"; chmod 777 "+event_file+";fi");
			execute_cmd(p_stdin, "if [ ! -e \""+control_event_file+"\" ]; then touch "+control_event_file+"; chmod 777 "+control_event_file+";fi");
			
			execute_cmd(p_stdin, "echo Geneva: text_data_transfer_file > " + control_event_file);			
			execute_cmd(p_stdin, "echo Format: 1 >> " + control_event_file);
			execute_cmd(p_stdin, "echo Character_set: ASCII8 >> " + control_event_file);
			execute_cmd(p_stdin, "echo File_type: control_file >> " + control_event_file);
			execute_cmd(p_stdin, "echo File_subtype: GENERATED_EVENTS >> " + control_event_file);
			execute_cmd(p_stdin, "echo File_group_number: 12 >> " + control_event_file);
			execute_cmd(p_stdin, "echo File_in_group_number: 0 >> " + control_event_file);
			execute_cmd(p_stdin, "echo Total_files_in_group: 1 >> " + control_event_file);
			execute_cmd(p_stdin, "echo Source_ID: source >> " + control_event_file);
			execute_cmd(p_stdin, "echo Tag: tag -v13 >> " + control_event_file);
			execute_cmd(p_stdin, "echo \"# This file produced by GTFplaintext 0.1\" >> " + control_event_file);
			execute_cmd(p_stdin, "echo File: \\\"event_file_sub_type_1.tlnt.im.darwin.sndbx.s41.novalocal_1910.1\\\",\\\"event_file\\\",\\\"event_file_sub_type\\\",\\\"1\\\",,,,, >> " + control_event_file);
			execute_cmd(p_stdin, "echo Footer: text_data_transfer_file >> " + control_event_file);
			execute_cmd(p_stdin, "echo AuditValue_1: 1 >> " + control_event_file);
			execute_cmd(p_stdin, "echo AuditValue_2: 0 >> " + control_event_file);
			execute_cmd(p_stdin, "echo End: text_data_transfer_file >> " + control_event_file);
			execute_cmd(p_stdin, "echo Lines: 16 >> " + control_event_file);
			execute_cmd(p_stdin, "echo Characters: 468 >> " + control_event_file);
			execute_cmd(p_stdin, "echo Checksum: >> " + control_event_file);
			execute_cmd(p_stdin, "echo Security_checksum: >> " + control_event_file);
			execute_cmd(p_stdin, "echo End_of_file: >> " + control_event_file);
			
			execute_cmd(p_stdin, "echo Geneva: text_data_transfer_file > " + event_file);
			execute_cmd(p_stdin, "echo Format: 1 >> " + event_file);
			execute_cmd(p_stdin, "echo Character_set: ASCII8 >> " + event_file);
			execute_cmd(p_stdin, "echo File_type: event_file >> " + event_file);
			execute_cmd(p_stdin, "echo File_subtype: event_file_sub_type >> " + event_file);
			execute_cmd(p_stdin, "echo File_group_number: 12 >> " + event_file);
			execute_cmd(p_stdin, "echo File_in_group_number: 1 >> " + event_file);
			execute_cmd(p_stdin, "echo Total_files_in_group: 16 >> " + event_file);
			execute_cmd(p_stdin, "echo Source_ID: source >> " + event_file);
			execute_cmd(p_stdin, "echo Tag: tag -v13 >> " + event_file);
			execute_cmd(p_stdin, "echo \"# Mobile Voice: From Belgium to Belgium\" >> " + event_file);
			execute_cmd(p_stdin, "echo Event: \\\""+phone_num+"\\\",\\\"1\\\",\\\""+event_time+"\\\",,,,,,,,,,,,\\\""+phone_num+"\\\",\\\"321546984\\\",\\\"480\\\",\\\"O\\\",,\\\"BEL\\\",,,,,,,,,,,,,,,,,, >> " + event_file);
			execute_cmd(p_stdin, "echo Event: \\\""+phone_num+"\\\",\\\"1\\\",\\\""+event_time+"\\\",,,,,,,,,,,,\\\""+phone_num+"\\\",\\\"321546054\\\",\\\"253\\\",\\\"O\\\",,\\\"BEL\\\",,,,,,,,,,,,,,,,,, >> " + event_file);
			execute_cmd(p_stdin, "echo Event: \\\""+phone_num+"\\\",\\\"1\\\",\\\""+event_time+"\\\",,,,,,,,,,,,\\\""+phone_num+"\\\",\\\"321540084\\\",\\\"545\\\",\\\"O\\\",,\\\"BEL\\\",,,,,,,,,,,,,,,,,, >> " + event_file);
			execute_cmd(p_stdin, "echo \"# Mobile Voice: From Belgium to Belgium On Net\" >> " + event_file);
			execute_cmd(p_stdin, "echo Event: \\\""+phone_num+"\\\",\\\"1\\\",\\\""+event_time+"\\\",,,,,,,,,,,,\\\""+phone_num+"\\\",\\\"321546984\\\",\\\"1100\\\",\\\"O\\\",\\\"32C4800\\\",\\\"BEL\\\",,,,,,,,,,,,,,,,,, >> " + event_file);
			execute_cmd(p_stdin, "echo Event: \\\""+phone_num+"\\\",\\\"1\\\",\\\""+event_time+"\\\",,,,,,,,,,,,\\\""+phone_num+"\\\",\\\"321546054\\\",\\\"553\\\",\\\"O\\\",\\\"32C4800\\\",\\\"BEL\\\",,,,,,,,,,,,,,,,,, >> " + event_file);
			execute_cmd(p_stdin, "echo Event: \\\""+phone_num+"\\\",\\\"1\\\",\\\""+event_time+"\\\",,,,,,,,,,,,\\\""+phone_num+"\\\",\\\"321540084\\\",\\\"445\\\",\\\"O\\\",\\\"32C4800\\\",\\\"BEL\\\",,,,,,,,,,,,,,,,,, >> " + event_file);
			execute_cmd(p_stdin, "echo Event: \\\""+phone_num+"\\\",\\\"1\\\",\\\""+event_time+"\\\",,,,,,,,,,,,\\\""+phone_num+"\\\",\\\"321541184\\\",\\\"1201\\\",\\\"O\\\",\\\"32C4800\\\",\\\"BEL\\\",,,,,,,,,,,,,,,,,, >> " + event_file);
			execute_cmd(p_stdin, "echo \"# Mobile Data: in Belgium\" >> " + event_file);
			execute_cmd(p_stdin, "echo Event: \\\""+phone_num+"\\\",\\\"7\\\",\\\""+event_time+"\\\",,,,,,,,,,,,\\\""+phone_num+"\\\",\\\"10971520\\\",\\\"10000000\\\",\\\"20971520\\\",\\\"gprs.base.be\\\",\\\"RG6\\\",\\\"32\\\",,,,,,,,,,,,,,,,, >> " + event_file);
			execute_cmd(p_stdin, "echo Footer: text_data_transfer_file >> " + event_file);
			execute_cmd(p_stdin, "echo AuditValue_1: 1 >> " + event_file);
			execute_cmd(p_stdin, "echo AuditValue_2: 0 >> " + event_file);
			execute_cmd(p_stdin, "echo End: text_data_transfer_file >> " + event_file);
			execute_cmd(p_stdin, "echo Lines: 15 >> " + event_file);
			execute_cmd(p_stdin, "echo Characters: 357 >> " + event_file);
			execute_cmd(p_stdin, "echo Checksum: >> " + event_file);
			execute_cmd(p_stdin, "echo Security_checksum: >> " + event_file);
			execute_cmd(p_stdin, "echo End_of_file: >> " + event_file);					
			
			execute_cmd(p_stdin, "exit");
			execute_cmd(p_stdin, "exit");
			
			String s="";
			while ((s = br.readLine()) != null){
				logs.add(s);				
			}
			String result ="SUCCESS";
			logs.add(result);
			p_stdin.close();
			br.close();
			return logs;
		}
		
		public List<String> exec_bg_cmd(String next_bill_date, String domain_id, String BANum) throws Exception {
			boolean res = false;
			List<String> logs = new ArrayList<String>();
			ProcessBuilder builder = new ProcessBuilder( "/bin/bash" );
			Process p=null;
			p = builder.start();
			BufferedWriter p_stdin = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));						
			execute_cmd(p_stdin, "su - " + RBMUser);
			execute_cmd(p_stdin, RBMPassword);			
			execute_cmd(p_stdin, "cd /u02/netcracker/rbm/" + RBMUser + "/infinys_root");
			execute_cmd(p_stdin, ". ./infinys.env");
			execute_cmd(p_stdin, "export GENEVA_FIXEDDATETIME='" + next_bill_date + "'");
			execute_cmd(p_stdin, "BG -dg " + domain_id + " -a \"-a '" + BANum + "'\"");
			execute_cmd(p_stdin, "ING -a '-ir 0'");
			execute_cmd(p_stdin, "exit");
			execute_cmd(p_stdin, "exit");
			
			String s="";
			while ((s = br.readLine()) != null){
				logs.add(s);
				if (s.contains("The Bill Generator is exiting normally having billed 1 accounts")){
					res = true;
				}
			}
			String result = res?"SUCCESS":"FAILURE";
			logs.add(result);
			p_stdin.close();
			br.close();
			return logs;
		}
		
		public List<String> exec_printed_bg_cmd(String next_bill_date, String domain_id, String BANum) throws Exception {
			List<String> logs = new ArrayList<String>();
			ProcessBuilder builder = new ProcessBuilder( "/bin/bash" );
			Process p=null;
			p = builder.start();
			BufferedWriter p_stdin = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));						
			execute_cmd(p_stdin, "su - " + RBMUser);
			execute_cmd(p_stdin, RBMPassword);			
			execute_cmd(p_stdin, "cd /u02/netcracker/rbm/" + RBMUser + "/infinys_root");
			execute_cmd(p_stdin, ". ./infinys.env");
			execute_cmd(p_stdin, "export GENEVA_FIXEDDATETIME='" + next_bill_date + "'");
			execute_cmd(p_stdin, "BG -dg " + domain_id + " -a \"-a '" + BANum + "'\"");
			execute_cmd(p_stdin, "ING -a '-ir 0'");
			execute_cmd(p_stdin, "BDW -m WORK -dg " + domain_id + " -a \"-formatterID 1 -bill\"");
			execute_cmd(p_stdin, "BDW -m COMPLETE -dg " + domain_id + " -a \"-formatterID 1 -bill\"");
			execute_cmd(p_stdin, "MFM -dg " + domain_id + " -a \"-plugInPath $INFINYS_ROOT/RB/lib/libGnvJIBP.so\"");
			execute_cmd(p_stdin, "exit");
			execute_cmd(p_stdin, "exit");
			
			String s="";
			while ((s = br.readLine()) != null){
				logs.add(s);
			}
			String result = "UNKNOWN";
			logs.add(result);
			p_stdin.close();
			br.close();
			return logs;
		}
		
		public void execute_cmd(BufferedWriter bw, String cmd) throws Exception {
			bw.write(cmd);
			bw.newLine();
			bw.flush();			
		}
		
		public void runPrepSql() throws Exception{
			execSQL("update gparams@RBM_dblink set string_value='F' where name='SYSusePredictiveRatingBoo'");
			execSQL("update gparams@RBM_dblink set integer_value=0 where name='CEIceStorageType'");
			execSQL("update gparams@RBM_dblink set string_value='F' where name='SYSIMDGinUseBoo'");
			execSQL("update gparams@RBM_dblink set name='SYSdateOverride' where name='#SYSdateOverride'");
			execSQL("update outputbinding@rbm_dblink set attr_10_out=null where attr_10_out=10494 and  output_binding_id in(1,2,3,4,5,6,7,8,9)");
			execSQL("merge into gparams@RBM_dblink t1 using (select 'SYSdateOverride' name, 'STRING' type, to_date('2010-01-01','yyyy-dd-mm') start_dtm, 'ANY' string_value from dual) t2 on (t1.name = t2.name) when not matched then insert (t1.name, t1.type, t1.start_dtm, t1.string_value) values(t2.name, t2.type, t2.start_dtm, t2.string_value)");			
			execSQL("update nc_directory set value = 'CostedEventsWithoutOCS'  where key ='com.netcracker.solutions.telenet.cbm.layer.service.event.EventServiceProvider'");
		}
		public void runPrepPrintSql() throws Exception{						
			execSQL("merge into tln_invnum_rule@RBM_dblink t1 using (select 2018 PROCESS_YEAR, 'UTB' BILL_STYLE, 18 YEAR_VALUE, 9 BILLTYPE_VALUE, 'UTB 2018' INVOICE_NUM_DESCRIPTION, 1 ROTATING_START_NUM, 1 ROTATING_END_NUM, 2098 FINAL_START_NUM, 184900000001 FINAL_END_NUM, 4 LEGAL_ENTITY from dual) t2 on (t1.INVOICE_NUM_DESCRIPTION = t2.INVOICE_NUM_DESCRIPTION) when not matched then insert (t1.PROCESS_YEAR, t1.BILL_STYLE, t1.YEAR_VALUE, t1.BILLTYPE_VALUE, t1.INVOICE_NUM_DESCRIPTION, t1.ROTATING_START_NUM, t1.ROTATING_END_NUM, t1.FINAL_START_NUM, t1.FINAL_END_NUM, t1.LEGAL_ENTITY) values(t2.PROCESS_YEAR, t2.BILL_STYLE, t2.YEAR_VALUE, t2.BILLTYPE_VALUE, t2.INVOICE_NUM_DESCRIPTION, t2.ROTATING_START_NUM, t2.ROTATING_END_NUM, t2.FINAL_START_NUM, t2.FINAL_END_NUM, t2.LEGAL_ENTITY)");
			execSQL("merge into tln_invnum_rule@RBM_dblink t1 using (select 2018 PROCESS_YEAR, 'CN' BILL_STYLE, 18 YEAR_VALUE, 6 BILLTYPE_VALUE, 'CN 2018' INVOICE_NUM_DESCRIPTION, 1 ROTATING_START_NUM, 1 ROTATING_END_NUM, 2000 FINAL_START_NUM, 183600000001 FINAL_END_NUM, 3 LEGAL_ENTITY from dual) t2 on (t1.INVOICE_NUM_DESCRIPTION = t2.INVOICE_NUM_DESCRIPTION) when not matched then insert (t1.PROCESS_YEAR, t1.BILL_STYLE, t1.YEAR_VALUE, t1.BILLTYPE_VALUE, t1.INVOICE_NUM_DESCRIPTION, t1.ROTATING_START_NUM, t1.ROTATING_END_NUM, t1.FINAL_START_NUM, t1.FINAL_END_NUM, t1.LEGAL_ENTITY) values(t2.PROCESS_YEAR, t2.BILL_STYLE, t2.YEAR_VALUE, t2.BILLTYPE_VALUE, t2.INVOICE_NUM_DESCRIPTION, t2.ROTATING_START_NUM, t2.ROTATING_END_NUM, t2.FINAL_START_NUM, t2.FINAL_END_NUM, t2.LEGAL_ENTITY)");			
		}
		
		public boolean isExist(String number) throws Exception {
			List<HashMap<String, String>> results = getResults("select account_num from account@RBM_dblink where account_num='" + number + "'");
			if (results.size() == 0) {
				return false;
			} else {
				return true;
			}				
		}
		
		public void execSQL(String query) throws Exception{
			Connection connection = null;
			Statement statement = null;
			ResultSet rs = null;
			try {
				connection = HomeInterfaceHelper.getInstance().getConnection();
				statement = connection.createStatement();
				statement.executeUpdate(query);				
			} finally {
				if (rs != null) {
						rs.close();
					}		
					if (statement != null) {
						statement.close();
					}		
					if (connection != null) {
						connection.close();
					}
			}
		}
		
		public List<HashMap<String, String>> getResults(String query) throws Exception {
			Connection connection = null;
			Statement statement = null;
			ResultSet rs = null;
			PreparedStatement st = null;
			List<String> headers = null;
			List<HashMap<String, String>> rows = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> row = null;
			try {
				connection = HomeInterfaceHelper.getInstance().getConnection();
				statement = connection.createStatement();
				rs = statement.executeQuery(query);
				headers = getHeaders(rs);
				while (rs.next()) {
					row = new HashMap<String, String>();					
					for (String header : headers) {
						row.put(header, rs.getString(header).trim());
					}
					rows.add(row);
				}
				
				return rows;
			} finally {
				if (rs != null) {
						rs.close();
					}
		
					if (statement != null) {
						statement.close();
					}
		
					if (connection != null) {
						connection.close();
					}
			}
		}
		
		private List<String> getHeaders(ResultSet rs) throws SQLException {
			List<String> headers = new ArrayList<String>();

			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				headers.add(rsmd.getColumnName(i).trim());
			}

			return headers;
		}

		public boolean exec_rbm_cmd(String cmd, String expected_result) throws Exception{
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));						
			String s = "a";
			boolean res = false;
			out.println("<tr><td>Debug:"+cmd+"</td></tr>");
			while ((s = br.readLine()) != null){
				out.println("<tr><td>Debug:"+s+"</td></tr>");
				if (s.contains(expected_result)){
					res = true;
				}
			}				
			p.waitFor(); 
			p.destroy();
			return res;
		
		}
		
		public boolean exec_rbm_cmd(String cmd) throws Exception{	
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));						
			String s = "a";
			out.println("<tr><td>Debug:"+cmd+"</td></tr>");
			while ((s = br.readLine()) != null){
				out.println("<tr><td>Debug:"+s+"</td></tr>");
			}				
			p.waitFor(); 
			p.destroy();
			return true;
		
		}

		public String check_endpoint(String host_ip_native, String host_port, String host_endpoint, boolean isSoap) throws Exception{
			if (host_ip_native.equals("Not Configured")||host_port.equals("Not Configured")||host_endpoint.equals("Not Configured")||host_ip_native.equals("localhost")){
				return "<span style=\"background-color:Bisque\">N/A</span>";
				//return "N/A";
			} else {
				try {
					String host_ip = host_ip_native.toLowerCase();
					String get_wsdl = "", key = "";					
					if (isSoap){
						if (host_ip.contains("http")){
							get_wsdl = host_ip + ":" + host_port + host_endpoint + "?wsdl";					
						} else {
							get_wsdl = "http://" + host_ip + ":" + host_port + host_endpoint + "?wsdl";
						}
						key = "-vvv";						
					} else {
						get_wsdl = "http://" + host_ip + ":" + host_port + host_endpoint;
						key = "-I";						
					}
					String[] check_cmd = {"timeout","5s","curl", key, get_wsdl};
					Process p = Runtime.getRuntime().exec(check_cmd);
					BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					
					String s = " ";
					String res = "<span style=\"background-color:red\">NOT OK</span>";
					//String res = "NOT OK";
					while ((s = br.readLine()) != null){
						if (s.contains("HTTP/1.1 200 OK")){
							res = "<span style=\"background-color:GreenYellow\">OK</span>";
							//res = "OK";
							break;
						}
					}
					p.waitFor(); 
					p.destroy();
					return res;
				} catch (IOException e) {
					e.printStackTrace();
					return "<span style=\"background-color:red\">NOT OK</span>";
				}
			}
		}		
		
	}

	class ThisPage extends UniPage {
		
		public String mode="";

		protected void parseParameters() {
		}

		@Override
		protected void init() throws Exception {
			setPageInfo("RBM Executor");        	
            ModernDesign theDesign = makeDesign();
            theDesign.setDocumentTitle("RBM Executor");
            NavigationPath path = new NavigationPath();
            path.addItem("RBM Executor");
            setPageDesign(theDesign);			
		}
		
		protected void collectSheets(){
			super.addCommonSheet(new RBMExecutorSheet());		 
		}
	}
%>
<%
	ThisPage thePage = new ThisPage();
	thePage.setPageContext(pageContext);
	thePage.debugSecureService();
	
%>