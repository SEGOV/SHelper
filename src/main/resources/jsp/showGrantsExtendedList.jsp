<%--
 This software is the confidential information and copyrighted work of
 NetCracker Technology Corp. ("NetCracker") and/or its suppliers and
 is only distributed under the terms of a separate license agreement
 with NetCracker.
 Use of the software is governed by the terms of the license agreement.
 Any use of this software not in accordance with the license agreement
 is expressly prohibited by law, and may result in severe civil
 and criminal penalties. 
 
 Copyright (c) 1995-2014 NetCracker Technology Corp.
 
 All Rights Reserved.
--%>

<%@ page import="com.netcracker.configuration.TxHelper" %>
<%@ page import="com.netcracker.ejb.attribute.Attribute" %>
<%@ page import="com.netcracker.ejb.attribute.AttributeHome" %>
<%@ page import="com.netcracker.ejb.attribute.AttributeSchema" %>
<%@ page import="com.netcracker.ejb.attribute.AttributeSchemaHome" %>
<%@ page import="com.netcracker.ejb.core.*" %>
<%@ page import="com.netcracker.ejb.core.users.UserData" %>
<%@ page import="com.netcracker.ejb.core.users.UserFacade" %>
<%@ page import="com.netcracker.ejb.framework.HomeInterfaceHelper" %>
<%@ page import="com.netcracker.ejb.framework.Logger" %>
<%@ page import="com.netcracker.ejb.session.security.NCSecurityCache" %>
<%@ page import="com.netcracker.ejb.session.security.Security" %>
<%@ page import="com.netcracker.framework.profiler.IDomain" %>
<%@ page import="com.netcracker.framework.profiler.Profiler" %>
<%@ page import="com.netcracker.jsp.CommonSheet" %>
<%@ page import="com.netcracker.jsp.ModernDesign" %>
<%@ page import="com.netcracker.jsp.NavigationPath" %>
<%@ page import="com.netcracker.jsp.UniPage" %>
<%@ page import="javax.ejb.EJBException" %>
<%@ page import="javax.transaction.xa.Xid" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.math.BigInteger" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.util.*" %>
<%@ page import="com.netcracker.rbac.GenericSecurityManageable" %>
<%@ page import="com.netcracker.framework.jdbc.JDBCTemplates" %>
<%@ page import="com.netcracker.framework.jdbc.ResultSetHandler" %>
<%@ page import="com.netcracker.ejb.framework.TypesConverter" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="com.netcracker.framework.jdbc.oracle.JDBCType" %>
<%@ page import="com.netcracker.ejb.core.users.UserHome" %>
<%@ page import="com.netcracker.ejb.core.users.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%!

class ThisSheet extends CommonSheet
{
	public ThisSheet() {}

	public final static int ACC_UNSPECIFIED = 0x00;
    public final static int ACC_GRANTED = 0x01;
    public final static int ACC_DENIED = 0x02;

    public final static int ACC_MASK = 0x03;

    public final static int ACC_LIST_NOT_SET = ACC_UNSPECIFIED;
    public final static int ACC_LIST_GRANT = ACC_GRANTED;
    public final static int ACC_LIST_DENY = ACC_DENIED;

    public final static int MASK_LIST = ACC_MASK;

    public final static int MASK_LIST_SHIFT = 0;

    public final static int ACC_READ_NOT_SET = ACC_UNSPECIFIED << 2;
    public final static int ACC_READ_GRANT = ACC_GRANTED << 2;
    public final static int ACC_READ_DENY = ACC_DENIED << 2;
    public final static int MASK_READ = ACC_MASK << 2;

    public final static int MASK_READ_SHIFT = 2;

    public final static int ACC_WRITE_NOT_SET = ACC_UNSPECIFIED << 4;
    public final static int ACC_WRITE_GRANT = ACC_GRANTED << 4;
    public final static int ACC_WRITE_DENY = ACC_DENIED << 4;
    public final static int MASK_WRITE = ACC_MASK << 4;
    public final static int MASK_WRITE_SHIFT = 4;

    public final static int ACC_DELETE_NOT_SET = ACC_UNSPECIFIED << 6;
    public final static int ACC_DELETE_GRANT = ACC_GRANTED << 6;
    public final static int ACC_DELETE_DENY = ACC_DENIED << 6;
    public final static int MASK_DELETE = ACC_MASK << 6;

    public final static int MASK_DELETE_SHIFT = 6;

    public final static int ACC_READ_GRANTS_NOT_SET = ACC_UNSPECIFIED << 8;
    public final static int ACC_READ_GRANTS_GRANT = ACC_GRANTED << 8;
    public final static int ACC_READ_GRANTS_DENY = ACC_DENIED << 8;
    public final static int MASK_READ_GRANTS = ACC_MASK << 8;
    public final static int MASK_READ_GRANTS_SHIFT = 8;

    public final static int ACC_WRITE_GRANTS_NOT_SET = ACC_UNSPECIFIED << 10;
    public final static int ACC_WRITE_GRANTS_GRANT = ACC_GRANTED << 10;
    public final static int ACC_WRITE_GRANTS_DENY = ACC_DENIED << 10;
    public final static int MASK_WRITE_GRANTS = ACC_MASK << 10;

    public final static int MASK_WRITE_GRANTS_SHIFT = 10;

    public final static int ACC_CREATE_NOT_SET = ACC_UNSPECIFIED << 12;
    public final static int ACC_CREATE_GRANT = ACC_GRANTED << 12;
    public final static int ACC_CREATE_DENY = ACC_DENIED << 12;
    public final static int MASK_CREATE = ACC_MASK << 12;

    public final static int MASK_CREATE_SHIFT = 12;

    public final static int ACC_OPCHILDREN_NOT_SET = ACC_UNSPECIFIED << 14;
    public final static int ACC_OPCHILDREN_GRANT = ACC_GRANTED << 14;
    public final static int ACC_OPCHILDREN_DENY = ACC_DENIED << 14;
    public final static int MASK_OPCHILDREN = ACC_MASK << 14;

    public final static int MASK_OPCHILDREN_SHIFT = 14;

    public final static int GRANTS_AMOUNT = 8;

    public final static int FLAG_NOT_SET = 0;
    public final static int FLAG_SET = 1;
    public final static int FLAG_MASK = 1;

    public final static int FLAG_RENEW_NOT_SET = FLAG_NOT_SET << 30;
    public final static int FLAG_RENEW_SET = FLAG_SET << 30;
    public final static int MASK_RENEW = FLAG_MASK << 30;
    public final static int MASK_RENEW_SHIFT = 30;

    private int indent = 0;
    private int count = 0;
    private String lastParent = null;
    private String lastRole = null;
    private String accessResult = null;
    
	BigInteger[][] matrix = new BigInteger[100][100];

    private StringBuffer log = new StringBuffer();

    private String userFieldValue(String user)
    {
    	String result = "";

    	if (user != null)
        {
            result += "value=\"" + user + "\"";
        }

        return result;
    }

    private String objectFieldValue(BigInteger id)
    {
        String result = "";

        if (id != null)
        {
            result += "value=" + id.toString();
        }

        return result;
    }

    private String renderOptionElement(int defaultMaskValue, String maskName, int selectedMaskValue)
    {
        String result = "<option ";
        String commonOptionTagPart = "value=\"" + defaultMaskValue + "\">" + maskName + "</option>";

        if (defaultMaskValue == selectedMaskValue)
        {
            result += " selected " + commonOptionTagPart;
        }
        else
        {
            result += commonOptionTagPart;
        }

        return result;
    }

    private int internalCheckAccess(int grants, int accessType)
    {
        int res = grants & accessType;

        switch (accessType)
        {
            case Security.MASK_LIST:
                break;
            case Security.MASK_READ:
                res = res >> 2;
                break;
            case Security.MASK_WRITE:
                res = res >> 4;
                break;
            case Security.MASK_DELETE:
                res = res >> 6;
                break;
            case Security.MASK_READ_GRANTS:
                res = res >> 8;
                break;
            case Security.MASK_WRITE_GRANTS:
                res = res >> 10;
                break;
            case Security.MASK_CREATE:
                res = res >> 12;
                break;
            case Security.MASK_OPCHILDREN:
                res = res >> 14;
                break;
            case Security.MASK_RENEW:
                res = res >> 30;
                break;
            default:
                return Security.ACC_UNSPECIFIED;
        }

        return res;
    }

    private SecurityManageble getSecurityManageble(BigInteger id)
    {
        try
        {
            NCObject obj = ReferenceResolver.resolveID(id);
            return obj;
        }
        catch (Exception e){}

        try
        {
            ObjectType obj = getObjectTypeHome().findByPrimaryKey(id);
            return obj;
        }
        catch (Exception e){}

        try
        {
            Attribute obj = getAttributeHome().findByPrimaryKey(id);
            return obj;
        }
        catch (Exception e){}

        try
        {
            AttributeSchema obj = getAttributeSchemaHome().findByPrimaryKey(id);
            return obj;
        }
        catch (Exception e){}

        throw new RuntimeException("Object not found = " + id);
    }

    protected AttributeHome getAttributeHome()
    {
        return (AttributeHome) HomeInterfaceHelper.getInstance().lookupHome("com.netcracker.ejb.attribute.AttributeHome", AttributeHome.class);
    }

    protected AttributeSchemaHome getAttributeSchemaHome()
    {
        return (AttributeSchemaHome) HomeInterfaceHelper.getInstance().lookupHome("com.netcracker.ejb.attribute.AttributeSchemaHome", AttributeSchemaHome.class);
    }

    protected ObjectTypeHome getObjectTypeHome()
    {
        return (ObjectTypeHome) HomeInterfaceHelper.getInstance().lookupHome("com.netcracker.ejb.core.ObjectTypeHome", ObjectTypeHome.class);
    }

    protected void printWindowContent() throws Exception
    {
    	String user = getParameter("user");
        BigInteger id = (getParameter("object") != null && !"".equals(getParameter("object"))) ? new BigInteger(getParameter("object").trim()) : null;
        int mask = (getParameter("mask") != null ? Integer.parseInt(getParameter("mask")) : 0);
        boolean isExplicitOnly = (getParameter("is_explicit_only") != null ? true : false);
        String checkbox = "";
        
        if (isExplicitOnly)
        	checkbox = "<input type=\"checkbox\" name=\"is_explicit_only\" value=\"explicit_only\" checked>";
        else
        	checkbox = "<input type=\"checkbox\" name=\"is_explicit_only\" value=\"explicit_only\">";
		
        out.println("<form action=\"showGrantsExtendedList.jsp\">\n" +
		                "<table>\n" +
		                "    <tr>\n" +
		                "        <td>User Name:</td>\n" +
		                "        <td colspan=\"2\"><input name=\"user\" type=\"text\" width=\"30\"" + userFieldValue(user) + "></td>\n" +
		                "    </tr>\n" +
		                "    <tr>\n" +
		                "        <td>ID (Object, Object Type or Attribute) </td>\n" +
		                "        <td colspan=\"2\"><input name=\"object\" type=\"text\" width=\"30\"" + objectFieldValue(id) + "></td>\n" +
		                "    </tr>\n" +
		                "    <tr>\n" +
		                "        <td>Grant:</td>\n" +
		                "        <td>" +
		                "        	<select name=mask id=mask size=1 >" +
		                renderOptionElement(MASK_WRITE, "MASK_WRITE", mask) +
		                renderOptionElement(MASK_LIST, "MASK_LIST", mask) +
		                renderOptionElement(MASK_READ, "MASK_READ", mask) +
		                renderOptionElement(MASK_CREATE, "MASK_CREATE", mask) +
		                renderOptionElement(MASK_OPCHILDREN, "MASK_OPCHILDREN", mask) +
		                renderOptionElement(MASK_DELETE, "MASK_DELETE", mask) +
		                renderOptionElement(MASK_READ_GRANTS, "MASK_READ_GRANTS", mask) +
		                renderOptionElement(MASK_WRITE_GRANTS, "MASK_WRITE_GRANTS", mask) +
		                renderOptionElement(MASK_RENEW, "MASK_RENEW", mask) +
		                "    		</select>" +
		                "        </td>\n" +
		                "        <td><input type=\"submit\" value=\"Calculate\"></td>\n" +
		                "    </tr>\n" +
		                "    <tr>\n" +
		                "        <td>Explicit grants only</td>\n" +
		                "        <td colspan=\"2\">" + 
									checkbox +
						"		</td>\n" +
                		"    </tr>\n" +
                		"</table>" +
                	"</form>");
		out.println("<button onclick=\"collapse()\">Collapse</button>"); 
		out.println("<button onclick=\"expand()\">Expand</button>");

        if (id != null && user != null && mask != 0)
        {
            SecurityManageble object = getSecurityManageble(id);

            levels.clear();

            Set<BigInteger> checkedNodes = new HashSet<BigInteger>();
			nextNode(null, object, user, mask, checkedNodes);

			out.println("<br>");
			out.println("<br>");

			printMatrix();

			calculateEffectiveGrants();

			levelsCount = 0;
			
			//out.println("<ul>");
			Set<BigInteger> printedNodes = new HashSet<BigInteger>();
			printHierarchy(levels.get(0).cur, isExplicitOnly, printedNodes);
			//out.println("</ul>");

			//out.println("<table>");
			//printMatrix2(isExplicitOnly);
			//out.println("</table>");

			out.println("<br>");
			out.println("<br>");
			
			printScriptJs();
		}
	}
	
    public String getName()
    {
        return "Grants Calculation";
    }

	private Level getLevelById(BigInteger id)
	{
		for (Level level: levels)
		{
			if (level.cur.equals(id))
				return level;
		}
		
		return null;
	}
	
	private void printMatrix() throws Exception
	{
		matrix[0][0] = levels.get(0).cur;
		count++;

		//int break1 = 0;
		int y = 0;
		int x = 0;
		
		while (count <= levels.size() && /*break1 != 100*/ x < 100)
		{
			y = updateMatrix(x, y);
			x++;
			//break1++;
		}
	}

    private int updateMatrix(int x, int y) throws Exception
	{
		for (int i = 0; i <= y; i++)
		{
			if (matrix[i][x] != null && !matrix[i][x].equals(BigInteger.valueOf(0L)))
			{
				int localJ = 0;
				int j = i;

				for (Level level : levels)
				{
					if (matrix[i][x].equals(level.prev))
					{
						matrix[j][x + 1] = level.cur;
						j++;
						localJ++;
						count++;
					}
				}

				if (localJ > 1)
				{
					updatePartMatrix(i + 1, x, y);
					i = i + localJ - 1;
					y = y + localJ - 1;
				}
			}
		}

		return y;
	}

	private void updatePartMatrix(int beginRow, int beforeColumn, int y) throws Exception
	{
		for (int i = y + 1; i > beginRow; i--)
		{
			for (int j = 0; j <= beforeColumn; j++)
			{
				matrix[i][j] = matrix[i - 1][j];
			}
		}

		for (int j = 0; j <= beforeColumn; j++)
		{
			matrix[beginRow][j] = BigInteger.valueOf(0L);
		}
	}
	
	public void printScriptJs()  throws Exception
	{
		out.println("<SCRIPT LANGUAGE=\"JavaScript\" TYPE=\"text/javascript\">");
		
		out.println("function expandElement(index) {");
		out.println("  var subObj = document.all['parents_' + index];");
		out.println("	if (subObj.style.display == 'none') {");
		out.println("	  subObj.style.display = 'block';");
		out.println("	}");
		out.println("	else {");
		out.println("	  subObj.style.display = 'none';");
		out.println("	}");
		out.println("}");

		out.println("function collapse() {");
		out.println("	for (i = 0; i <= " + levelsCount +"; i++) {");
		out.println("		var subObj = document.all['parents_' + i];");
		out.println("		subObj.style.display = 'none';");
		out.println("	}");
		out.println("}");
		
		out.println("function expand() {");
		out.println("	for (i = 0; i <= " + levelsCount +"; i++) {");
		out.println("		var subObj = document.all['parents_' + i];");
		out.println("		subObj.style.display = 'block';");
		out.println("	}");
		out.println("}");
		
		out.println("</SCRIPT>");
	}
	
	public int levelsCount = 0;
	
	public void printHierarchy(BigInteger objectId, boolean isExplicitOnly, Set<BigInteger> printedNodes) throws Exception
	{
		if (objectId != null)
		{
			for (Level level: levels)
			{
				if (level.cur.equals(objectId))
				{
					printMatrix3(isExplicitOnly, level, levelsCount);
					printedNodes.add(objectId);
					out.println("<ul id=\"parents_" + levelsCount + "\">");
					
					levelsCount++;
					
					if (level.primarySecurityParentId != null)
					{
						out.println("<lh><b>Primary parent:</b></lh>");
						printHierarchy(level.primarySecurityParentId, isExplicitOnly, printedNodes);
					}
					
					if (level.secondarySecurityParentId != null)
					{
						out.println("<lh><b>Secondary parent:</b></lh>");
						printHierarchy(level.secondarySecurityParentId, isExplicitOnly, printedNodes);
					}
					
					if (level.extendedSecurityParents != null)
					{
						for (Map.Entry<BigInteger, List<GenericSecurityManageable>> parent: level.extendedSecurityParents.entrySet())
						{
							List<GenericSecurityManageable> extendedParents = parent.getValue();
							
							for (GenericSecurityManageable extendedParent:extendedParents)
							{
								if (extendedParent != null)
								{
								    if (!printedNodes.contains(extendedParent.getSecurityID())) {
                                        printedNodes.add(extendedParent.getSecurityID());
                                        out.println("<lh><b>Extended parent:</b></lh>");
                                        printHierarchy(extendedParent.getSecurityID(), isExplicitOnly, printedNodes);
									}
								}
							}
						}
					}
					
					out.println("</ul>");
					
					break;
				}
			}
		}
	}
	
	private void printMatrix3(boolean isExplicitOnly, Level level, int levelNumber) throws Exception
	{
		if (isExplicitOnly)
		{
			if (level.explicitGrantsByMask == 1)
				out.println("<li style=\"background-color:#32CD32; width:200px; padding:5px; margin:5px;\" onclick=\"expandElement(" + levelNumber + ")\">");
			else if (level.explicitGrantsByMask == 2)
				out.println("<li style=\"background-color:#FF6347; width:200px; padding:5px; margin:5px;\" onclick=\"expandElement(" + levelNumber + ")\">");
			else if (level.explicitGrantsByMask == 0)
				out.println("<li style=\"background-color:#BEBEBE; width:200px; padding:5px; margin:5px;\" onclick=\"expandElement(" + levelNumber + ")\">");
		}
		else
		{
			if (level.effectiveGrantsByMask == 1)
				out.println("<li style=\"background-color:#32CD32; width:200px; padding:5px; margin:5px;\" onclick=\"expandElement(" + levelNumber + ")\">");
			else if (level.effectiveGrantsByMask == 2 || level.effectiveGrantsByMask == 0)
				out.println("<li style=\"background-color:#FF6347; width:200px; padding:5px; margin:5px;\" onclick=\"expandElement(" + levelNumber + ")\">");
		}

		if ("ObjectType".equals(level.type))
		{
			out.println("<a href=\"/admin/attr/otyperights.jsp?schema=1&objtype=" + level.cur + "&useAT=2\">" + level.type + "</a>");
		}
		
		if ("Object".equals(level.type))
		{
			out.println("<a href=\"/security/commonrights.jsp?cmd=8020752878013860715&object=" + level.cur + "\">" + level.type + "</a>");
		}
		
		if ("AttributeSchema".equals(level.type))
		{
			out.println("<a href=\"/admin/attr/aschemarights.jsp?objtype=0&schema=" + level.cur + "&useAT=2\">" + level.type + "</a>");
		}
		
		if ("Attribute".equals(level.type))
		{
			out.println("<a href=\"/admin/attr/attrrights.jsp?schema=1&objtype=0&attr=" + level.cur + "&useAT=2\">" + level.type + "</a>");
		}

		out.println("<br>");
		out.println(" " + level.name + " ");
		out.println("<br>");
		out.println(" " + level.cur + " ");
		
		if (level.renewGrants != 0)
		{
			out.println("<br>");
			out.println("<font color=\"white\">RENEW = Allow</font>");
		}

		out.println("</li>");
	}

	private void printMatrix2(boolean isExplicitOnly) throws Exception
	{
		for (int j = 20; j >= 0; j--)
		{
			out.println("<tr>");

			for (int i = 0; i < 20; i++)
			{
				if (matrix[i][j] != null && !BigInteger.valueOf(0L).equals(matrix[i][j]))
				{
					if (isExplicitOnly)
					{
						if (getLevelById(matrix[i][j]).explicitGrantsByMask == 1)
							out.println("<td height=\"62\" weidth=\"62\" bgcolor=\"#32CD32\">");
						else if (getLevelById(matrix[i][j]).explicitGrantsByMask == 2)
							out.println("<td height=\"62\" weidth=\"62\" bgcolor=\"#FF6347\">");
						else if (getLevelById(matrix[i][j]).explicitGrantsByMask == 0)
							out.println("<td height=\"62\" weidth=\"62\" bgcolor=\"#BEBEBE\">");
					}
					else
					{
						if (getLevelById(matrix[i][j]).effectiveGrantsByMask == 1)
							out.println("<td height=\"62\" weidth=\"62\" bgcolor=\"#32CD32\">");
						else if (getLevelById(matrix[i][j]).effectiveGrantsByMask == 2 || getLevelById(matrix[i][j]).effectiveGrantsByMask == 0)
							out.println("<td height=\"62\" weidth=\"62\" bgcolor=\"#FF6347\">");
					}

					if ("ObjectType".equals(getLevelById(matrix[i][j]).type))
					{
						out.println("<a href=\"/admin/attr/otyperights.jsp?schema=1&objtype=" + getLevelById(matrix[i][j]).cur + "&useAT=2\">" + getLevelById(matrix[i][j]).type + "</a>");
					}
					
					if ("Object".equals(getLevelById(matrix[i][j]).type))
					{
						out.println("<a href=\"/security/commonrights.jsp?cmd=8020752878013860715&object=" + getLevelById(matrix[i][j]).cur + "\">" + getLevelById(matrix[i][j]).type + "</a>");
					}
					
					if ("AttributeSchema".equals(getLevelById(matrix[i][j]).type))
					{
						out.println("<a href=\"/admin/attr/aschemarights.jsp?objtype=0&schema=" + getLevelById(matrix[i][j]).cur + "&useAT=2\">" + getLevelById(matrix[i][j]).type + "</a>");
					}
					
					if ("Attribute".equals(getLevelById(matrix[i][j]).type))
					{
						out.println("<a href=\"/admin/attr/attrrights.jsp?schema=1&objtype=0&attr=" + getLevelById(matrix[i][j]).cur + "&useAT=2\">" + getLevelById(matrix[i][j]).type + "</a>");
					}

					out.println("<br>");
					out.println(" " + getLevelById(matrix[i][j]).name + " ");
					out.println("<br>");
					out.println(" " + getLevelById(matrix[i][j]).cur + " ");
					
					if (getLevelById(matrix[i][j]).renewGrants != 0)
					{
						out.println("<br>");
						out.println("<font color=\"white\">RENEW = Allow</font>");
					}

					out.println("</td>");
				}
				else
				{
					out.println("<td></td>");
				}
			}
			out.println("</tr>");
		}
	}

	private void nextNode(BigInteger prevId, SecurityManageble object, String userName, int mask, Set<BigInteger> checkedNodes) throws Exception
	{
		int grants = getGrantsFromDBForAllRoles(object.getSecurityID(), userName, mask);
		int renewGrants = getGrantsFromDBForAllRoles(object.getSecurityID(), userName, MASK_RENEW);

		Level curObject = new Level(prevId, object.getSecurityID(), grants, renewGrants);

		levels.add(curObject);

		ArrayList<SecurityManageble> securityParents = new ArrayList<SecurityManageble>();
		ArrayList<SecurityManageble> tempList = new ArrayList<SecurityManageble>();

		SecurityManageble primarySecurityParent = object.getPrimaryParentSecurityManageble();
		SecurityManageble secondarySecurityParent = object.getSecondaryParentSecurityManageble();

		securityParents.add(primarySecurityParent);

		if (primarySecurityParent != null)
		{
			curObject.primarySecurityParentId = primarySecurityParent.getSecurityID();
		}
		
		securityParents.add(secondarySecurityParent);
		
		if (secondarySecurityParent != null)
		{
			curObject.secondarySecurityParentId = secondarySecurityParent.getSecurityID();
		}
		
		Map<BigInteger, List<GenericSecurityManageable>> extendedSecurityParents = ((GenericSecurityManageable)object).getGenericSecurityParents();
		curObject.extendedSecurityParents = extendedSecurityParents;
		
		if (extendedSecurityParents != null)
		{
			for (Map.Entry<BigInteger, List<GenericSecurityManageable>> parent: extendedSecurityParents.entrySet())
			{
				tempList.addAll(parent.getValue());
			}
			securityParents.addAll(tempList);
		}

		if (prevId == null)
            checkedNodes.add(object.getSecurityID());

		for (SecurityManageble sm: securityParents)
		{
			if (sm != null && !checkedNodes.contains(sm.getSecurityID())) {
            	checkedNodes.add(sm.getSecurityID());
				nextNode(object.getSecurityID(), sm, userName, mask, checkedNodes);
            }
		}
	}

    private void getGrants(Level level, Set<BigInteger> checkedParents) throws Exception
    {
        if (level != null)
        {
            if (level.explicitGrantsByMask == 0)
            {
                int resultGrants = 0;
                Map<BigInteger, List<GenericSecurityManageable>> securityParents = level.extendedSecurityParents;
                List<Integer> securityParentsGrants = new ArrayList<Integer>();
                int renew = 0;

                if (level.secondarySecurityParentId != null)
                {
                    securityParentsGrants.add(getLevelById(level.secondarySecurityParentId).effectiveGrantsByMask);
                    renew = getLevelById(level.secondarySecurityParentId).renewGrants;
                }

                if (renew == 0)
                {
                    int objRenew = level.renewGrants;

                    if (objRenew == 0)
                    {
                        BigInteger primarySecurityParentId = level.primarySecurityParentId;

                        if (primarySecurityParentId != null)
                            securityParentsGrants.add(getLevelById(primarySecurityParentId).effectiveGrantsByMask);
                    }

                    if (securityParents != null)
                    {
                        for (Map.Entry<BigInteger, List<GenericSecurityManageable>> securityParent: securityParents.entrySet())
                        {
                            int extendedParentGrants = Security.ACC_UNSPECIFIED;

                            if (securityParent != null)
                            {
                                BigInteger extendedParentID = null;
                                List<GenericSecurityManageable> extendedParents = securityParent.getValue();

                                boolean allow = false;
                                boolean deny = false;

                                for (GenericSecurityManageable extendedParent:extendedParents)
                                {
                                    if (extendedParent != null)
                                    {
                                        extendedParentID = extendedParent.getSecurityID();

                                        if (extendedParentID != null)
                                        {
                                            if (!checkedParents.contains(extendedParentID))
                                            {
                                                checkedParents.add(extendedParentID);
                                                Level lev = getLevelById(extendedParentID);
                                                getGrants(lev, checkedParents);
                                                extendedParentGrants = lev.effectiveGrantsByMask;
                                            }
                                        }

                                        if (extendedParentGrants == Security.ACC_DENIED) {
                                            deny = true;
                                        }

                                        if (extendedParentGrants == Security.ACC_GRANTED) {
                                            allow = true;
                                        }
                                    }
                                }

                                if (allow) {
                                    extendedParentGrants = Security.ACC_GRANTED;
                                }
                                else if (deny) {
                                    extendedParentGrants = Security.ACC_DENIED;
                                }

                                securityParentsGrants.add(extendedParentGrants);
                            }
                        }
                    }
                }

                if (securityParentsGrants.contains(Security.ACC_DENIED))
                    level.effectiveGrantsByMask = ACC_DENIED;
                else if (securityParentsGrants.contains(ACC_GRANTED))
                    level.effectiveGrantsByMask = ACC_GRANTED;
            }
            else
            {
                level.effectiveGrantsByMask = level.explicitGrantsByMask;
            }
        }
    }

	private void calculateEffectiveGrants() throws Exception
	{
	    Set<BigInteger> checkedParents = new HashSet<BigInteger>();
		for (int j = 90; j >= 0; j--)
		{
			for(int i = 0; i <= 90; i++)
			{
				if (matrix[i][j] != null && !BigInteger.valueOf(0L).equals(matrix[i][j]))
				{
					int[] a = new int[1];
					a[0] = 1;
					Level level = getLevelById(matrix[i][j]);

					getGrants(level, checkedParents);
				}
			}
		}
	}
	
	private int _getGrants(BigInteger objectId, BigInteger roleId)
	{
		JDBCTemplates jdbc = NCCoreInternals.jdbcInstance();
		final String getGrants = "select grants from nc_grants where object_id = ? and user_id = ?";
		BigInteger grants = (BigInteger) jdbc.executeSelect(getGrants, new Object[][]{{objectId, JDBCType.NUMBER}, {roleId, JDBCType.NUMBER}}, new ResultSetHandler()
		{
			public Object onResultSet(ResultSet resultSet) throws SQLException
			{
				BigInteger grants = null;
				while (resultSet.next())
				{
					grants = TypesConverter.bigDecimalToBigInteger(resultSet.getBigDecimal("grants"));
				}
				return grants;
			}
		});

		if (grants != null)
			return grants.intValue();
		else
			return 0;
    }
	
	public int getGrantsFromDBForAllRoles(BigInteger objectId, String userName, int mask) throws Exception
	{
		final UserHome uHome = (UserHome) HomeInterfaceHelper.getInstance().lookupName("com.netcracker.ejb.core.users.UserHome");
		User startUser = uHome.findByName(userName);

        LinkedList stack = new LinkedList();
        HashSet handledUsers = new HashSet();

        stack.addLast(startUser);

        int denyCount = 0;

		while (!stack.isEmpty())
		{
			User user = (User) stack.removeFirst();

            if (handledUsers.contains(user.getID()))
			{
				continue;
			}

			handledUsers.add(user.getID());

            int grants = _getGrants(objectId, user.getID());

			if (grants != ACC_UNSPECIFIED)
			{
				grants = internalCheckAccess(grants, mask);

				switch (grants)
				{
					case ACC_GRANTED:
						return ACC_GRANTED;
					case ACC_DENIED:
						denyCount++;
						continue;
				}
			}

			Collection roles = user.getRoles();

			if (roles != null)
			{
				int size = roles.size();
				Iterator it = roles.iterator();

				for (int i = 0; i < size; i++)
				{
					BigInteger childRole = (BigInteger) it.next();
					User child = uHome.findByPrimaryKey(childRole);
					stack.addLast(child);
				}
			}
		}

		if (denyCount > 0)
		{
			return ACC_DENIED;
		}

		return ACC_UNSPECIFIED;
	}
}

class LegendSheet extends CommonSheet
{
    protected void printWindowContent() throws Exception
    {
        out.println("<iframe src=\"http://bass.netcracker.com/display/RnD/Security+matrix\" width=\"100%\" height=\"100%\"></iframe>");
        out.println("If you have some problem with page rendering go to-><br>");
        out.println("<a href=\"http://bass.netcracker.com/display/RnD/Security+matrix\"> http://bass.netcracker.com/display/RnD/Security+matrix </a><br><br>");
    }

    public String getName()
    {
        return "Legend";
    }
}


class GrantsCalculationPage extends UniPage
{
    GrantsCalculationPage() {}

    protected void init() throws Exception
    {
        ModernDesign theDesign = new ModernDesign();
        NavigationPath path = new NavigationPath();
        path.addItem("Grants calculator");
        theDesign.setNavigationPath(path);
        setPageDesign(theDesign);
        super.init();
    }

    protected void collectSheets()
    {
        addCommonSheet(new ThisSheet());
        addCommonSheet(new LegendSheet());
        super.collectSheets();
    }
}

ArrayList<Level> levels = new ArrayList<Level>();

class Level
{
	Map<BigInteger, Level> hyerachy = null;
	BigInteger prev;
	BigInteger cur;
	int explicitGrantsByMask = 0;
	int effectiveGrantsByMask = 0;
	int grants = 0;
	int renewGrants = 0;
	BigInteger primarySecurityParentId;
	BigInteger secondarySecurityParentId;
	Map<BigInteger, List<GenericSecurityManageable>> extendedSecurityParents = null;
	String type;
	String name;

	protected ObjectTypeHome getObjectTypeHome() {
		return (ObjectTypeHome) HomeInterfaceHelper.getInstance().lookupHome("com.netcracker.ejb.core.ObjectTypeHome", ObjectTypeHome.class);
	}

	protected AttributeHome getAttributeHome() {
		return (AttributeHome) HomeInterfaceHelper.getInstance().lookupHome("com.netcracker.ejb.attribute.AttributeHome", AttributeHome.class);
	}
	
	protected AttributeSchemaHome getAttributeSchemaHome() {
		return (AttributeSchemaHome) HomeInterfaceHelper.getInstance().lookupHome("com.netcracker.ejb.attribute.AttributeSchemaHome", AttributeSchemaHome.class);
	}

	protected Level(BigInteger prev, BigInteger cur, int grants, int renewGrants)
	{
		this.prev = prev;
		this.cur = cur;
		this.grants = grants;
		this.explicitGrantsByMask = grants;
		this.renewGrants = renewGrants;

		try
		{
			NCObject obj = ReferenceResolver.resolveID(cur);
			type = "Object";
			name = obj.getName();
		}
		catch (Exception e){}

		try
		{
			ObjectType obj = getObjectTypeHome().findByPrimaryKey(cur);
			type = "ObjectType";
			name = obj.getName();
		}
		catch (Exception e){}

		try
		{
			Attribute obj = getAttributeHome().findByPrimaryKey(cur);
			type = "Attribute";
			name = obj.getName();
		}
		catch (Exception e){}

		try
		{
			AttributeSchema obj = getAttributeSchemaHome().findByPrimaryKey(cur);
			type = "AttributeSchema";
			name = obj.getName();
		}
		catch (Exception e){}
	}
}
%>

<%
    GrantsCalculationPage myPage = new GrantsCalculationPage();
    myPage.setPageContext(pageContext);
    try
    {
    	myPage.debugSecureService();
    }
    finally{}
%>
<%--
 WITHOUT LIMITING THE FOREGOING, COPYING, REPRODUCTION, REDISTRIBUTION,
 REVERSE ENGINEERING, DISASSEMBLY, DECOMPILATION OR MODIFICATION
 OF THE SOFTWARE IS EXPRESSLY PROHIBITED, UNLESS SUCH COPYING,
 REPRODUCTION, REDISTRIBUTION, REVERSE ENGINEERING, DISASSEMBLY,
 DECOMPILATION OR MODIFICATION IS EXPRESSLY PERMITTED BY THE LICENSE
 AGREEMENT WITH NETCRACKER. 
 
 THIS SOFTWARE IS WARRANTED, IF AT ALL, ONLY AS EXPRESSLY PROVIDED IN
 THE TERMS OF THE LICENSE AGREEMENT, EXCEPT AS WARRANTED IN THE
 LICENSE AGREEMENT, NETCRACKER HEREBY DISCLAIMS ALL WARRANTIES AND
 CONDITIONS WITH REGARD TO THE SOFTWARE, WHETHER EXPRESS, IMPLIED
 OR STATUTORY, INCLUDING WITHOUT LIMITATION ALL WARRANTIES AND
 CONDITIONS OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 TITLE AND NON-INFRINGEMENT.
 
 Copyright (c) 1995-2014 NetCracker Technology Corp.
 
 All Rights Reserved.
--%>