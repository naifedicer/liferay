package org.apache.jsp.html.portlet.journal;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.captcha.CaptchaMaxChallengesException;
import com.liferay.portal.kernel.captcha.CaptchaTextException;
import com.liferay.portal.kernel.bean.BeanParamUtil;
import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.cal.Recurrence;
import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.DAOParamUtil;
import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.dao.search.ResultRow;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.dao.search.ScoreSearchEntry;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.dao.search.SearchEntry;
import com.liferay.portal.kernel.dao.search.TextSearchEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.io.unsync.UnsyncStringReader;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.language.LanguageWrapper;
import com.liferay.portal.kernel.language.UnicodeLanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.log.LogUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.portlet.DynamicRenderRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletMode;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.servlet.ImageServletTokenUtil;
import com.liferay.portal.kernel.servlet.ServletContextUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.servlet.StringServletResponse;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.BooleanWrapper;
import com.liferay.portal.kernel.util.BreadcrumbsUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.CalendarUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.KeyValuePairComparator;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.LongWrapper;
import com.liferay.portal.kernel.util.MathUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.ObjectValuePairComparator;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderedProperties;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PrefsParamUtil;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Randomizer;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.SortedProperties;
import com.liferay.portal.kernel.util.StackTraceUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.TimeZoneUtil;
import com.liferay.portal.kernel.util.UnicodeFormatter;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.language.LanguageImpl;
import com.liferay.portal.model.*;
import com.liferay.portal.model.impl.*;
import com.liferay.portal.security.auth.AuthTokenUtil;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.*;
import com.liferay.portal.service.permission.GroupPermissionUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.service.permission.LayoutPrototypePermissionUtil;
import com.liferay.portal.service.permission.LayoutSetPrototypePermissionUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.struts.StrutsUtil;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.ContentUtil;
import com.liferay.portal.util.CookieKeys;
import com.liferay.portal.util.JavaScriptBundleUtil;
import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletCategoryKeys;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.SessionClicks;
import com.liferay.portal.util.SessionTreeJSClicks;
import com.liferay.portal.util.ShutdownUtil;
import com.liferay.portal.util.WebAppPool;
import com.liferay.portal.util.WebKeys;
import com.liferay.portal.util.comparator.PortletCategoryComparator;
import com.liferay.portal.util.comparator.PortletTitleComparator;
import com.liferay.portlet.InvokerPortlet;
import com.liferay.portlet.PortalPreferences;
import com.liferay.portlet.PortletConfigFactoryUtil;
import com.liferay.portlet.PortletInstanceFactoryUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletResponseImpl;
import com.liferay.portlet.PortletSetupUtil;
import com.liferay.portlet.PortletURLImpl;
import com.liferay.portlet.PortletURLUtil;
import com.liferay.portlet.RenderParametersPool;
import com.liferay.portlet.RenderRequestFactory;
import com.liferay.portlet.RenderRequestImpl;
import com.liferay.portlet.RenderResponseFactory;
import com.liferay.portlet.RenderResponseImpl;
import com.liferay.portlet.UserAttributes;
import com.liferay.portlet.portletconfiguration.util.PortletConfigurationUtil;
import com.liferay.util.CreditCard;
import com.liferay.util.Encryptor;
import com.liferay.util.JS;
import com.liferay.util.PKParser;
import com.liferay.util.PwdGenerator;
import com.liferay.util.State;
import com.liferay.util.StateUtil;
import com.liferay.util.TextFormatter;
import com.liferay.util.UniqueList;
import com.liferay.util.format.PhoneNumberUtil;
import com.liferay.util.log4j.Levels;
import com.liferay.util.mail.InternetAddressUtil;
import com.liferay.util.portlet.PortletRequestUtil;
import com.liferay.util.servlet.DynamicServletRequest;
import com.liferay.util.servlet.SessionParameters;
import com.liferay.util.servlet.UploadException;
import com.liferay.util.xml.XMLFormatter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.portlet.MimeResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;
import javax.portlet.UnavailableException;
import javax.portlet.ValidatorException;
import javax.portlet.WindowState;
import org.apache.commons.math.util.MathUtils;
import org.apache.struts.Globals;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.XPath;
import com.liferay.portal.util.LayoutLister;
import com.liferay.portal.util.LayoutView;
import com.liferay.portlet.asset.NoSuchEntryException;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.util.DLUtil;
import com.liferay.portlet.journal.ArticleContentException;
import com.liferay.portlet.journal.ArticleDisplayDateException;
import com.liferay.portlet.journal.ArticleExpirationDateException;
import com.liferay.portlet.journal.ArticleIdException;
import com.liferay.portlet.journal.ArticleSmallImageNameException;
import com.liferay.portlet.journal.ArticleSmallImageSizeException;
import com.liferay.portlet.journal.ArticleTitleException;
import com.liferay.portlet.journal.ArticleTypeException;
import com.liferay.portlet.journal.ArticleVersionException;
import com.liferay.portlet.journal.DuplicateArticleIdException;
import com.liferay.portlet.journal.DuplicateFeedIdException;
import com.liferay.portlet.journal.DuplicateStructureIdException;
import com.liferay.portlet.journal.DuplicateTemplateIdException;
import com.liferay.portlet.journal.FeedContentFieldException;
import com.liferay.portlet.journal.FeedDescriptionException;
import com.liferay.portlet.journal.FeedIdException;
import com.liferay.portlet.journal.FeedNameException;
import com.liferay.portlet.journal.FeedTargetLayoutFriendlyUrlException;
import com.liferay.portlet.journal.FeedTargetPortletIdException;
import com.liferay.portlet.journal.NoSuchArticleException;
import com.liferay.portlet.journal.NoSuchStructureException;
import com.liferay.portlet.journal.NoSuchTemplateException;
import com.liferay.portlet.journal.RequiredStructureException;
import com.liferay.portlet.journal.RequiredTemplateException;
import com.liferay.portlet.journal.StructureDescriptionException;
import com.liferay.portlet.journal.StructureIdException;
import com.liferay.portlet.journal.StructureInheritanceException;
import com.liferay.portlet.journal.StructureNameException;
import com.liferay.portlet.journal.StructureXsdException;
import com.liferay.portlet.journal.TemplateDescriptionException;
import com.liferay.portlet.journal.TemplateIdException;
import com.liferay.portlet.journal.TemplateNameException;
import com.liferay.portlet.journal.TemplateSmallImageNameException;
import com.liferay.portlet.journal.TemplateSmallImageSizeException;
import com.liferay.portlet.journal.TemplateXslException;
import com.liferay.portlet.journal.action.EditArticleAction;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.model.JournalArticleDisplay;
import com.liferay.portlet.journal.model.JournalArticleResource;
import com.liferay.portlet.journal.model.JournalFeed;
import com.liferay.portlet.journal.model.JournalFeedConstants;
import com.liferay.portlet.journal.model.JournalStructure;
import com.liferay.portlet.journal.model.JournalStructureConstants;
import com.liferay.portlet.journal.model.JournalTemplate;
import com.liferay.portlet.journal.model.JournalTemplateConstants;
import com.liferay.portlet.journal.model.impl.JournalArticleImpl;
import com.liferay.portlet.journal.search.ArticleDisplayTerms;
import com.liferay.portlet.journal.search.ArticleSearch;
import com.liferay.portlet.journal.search.ArticleSearchTerms;
import com.liferay.portlet.journal.search.FeedDisplayTerms;
import com.liferay.portlet.journal.search.FeedSearch;
import com.liferay.portlet.journal.search.FeedSearchTerms;
import com.liferay.portlet.journal.search.StructureDisplayTerms;
import com.liferay.portlet.journal.search.StructureSearch;
import com.liferay.portlet.journal.search.StructureSearchTerms;
import com.liferay.portlet.journal.search.TemplateDisplayTerms;
import com.liferay.portlet.journal.search.TemplateSearch;
import com.liferay.portlet.journal.search.TemplateSearchTerms;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalFeedLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalStructureLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalTemplateLocalServiceUtil;
import com.liferay.portlet.journal.service.permission.JournalArticlePermission;
import com.liferay.portlet.journal.service.permission.JournalFeedPermission;
import com.liferay.portlet.journal.service.permission.JournalPermission;
import com.liferay.portlet.journal.service.permission.JournalStructurePermission;
import com.liferay.portlet.journal.service.permission.JournalTemplatePermission;
import com.liferay.portlet.journal.util.JournalUtil;
import com.liferay.portlet.journal.webdav.JournalWebDAVStorageImpl;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;
import com.liferay.util.RSSUtil;

public final class article_005faction_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(27);
    _jspx_dependants.add("/html/portlet/journal/init.jsp");
    _jspx_dependants.add("/html/portlet/init.jsp");
    _jspx_dependants.add("/html/common/init.jsp");
    _jspx_dependants.add("/html/common/init-ext.jsp");
    _jspx_dependants.add("/html/portlet/init-ext.jsp");
    _jspx_dependants.add("/WEB-INF/tld/displaytag.tld");
    _jspx_dependants.add("/WEB-INF/tld/c.tld");
    _jspx_dependants.add("/WEB-INF/tld/fmt.tld");
    _jspx_dependants.add("/WEB-INF/tld/fn.tld");
    _jspx_dependants.add("/WEB-INF/tld/sql.tld");
    _jspx_dependants.add("/WEB-INF/tld/x.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-portlet.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-aui.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-portlet-ext.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-security.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-theme.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-ui.tld");
    _jspx_dependants.add("/WEB-INF/tld/liferay-util.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-bean.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-bean-el.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-html.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-html-el.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-logic.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-logic-el.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-nested.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-tiles.tld");
    _jspx_dependants.add("/WEB-INF/tld/struts-tiles-el.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005ftarget_005fimage_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005ftarget_005fimage_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu.release();
    _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005ftarget_005fimage_005fnobody.release();
    _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.release();
    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;


/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

      out.write('\n');
      out.write('\n');

/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

      out.write('\n');
      out.write('\n');

/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

      out.write('\n');
      out.write('\n');

/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      //  liferay-theme:defineObjects
      com.liferay.taglib.theme.DefineObjectsTag _jspx_th_liferay_002dtheme_005fdefineObjects_005f0 = (com.liferay.taglib.theme.DefineObjectsTag) _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.get(com.liferay.taglib.theme.DefineObjectsTag.class);
      _jspx_th_liferay_002dtheme_005fdefineObjects_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dtheme_005fdefineObjects_005f0.setParent(null);
      int _jspx_eval_liferay_002dtheme_005fdefineObjects_005f0 = _jspx_th_liferay_002dtheme_005fdefineObjects_005f0.doStartTag();
      if (_jspx_th_liferay_002dtheme_005fdefineObjects_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.reuse(_jspx_th_liferay_002dtheme_005fdefineObjects_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dtheme_005fdefineObjects_005fnobody.reuse(_jspx_th_liferay_002dtheme_005fdefineObjects_005f0);
      com.liferay.portal.theme.ThemeDisplay themeDisplay = null;
      com.liferay.portal.model.Company company = null;
      com.liferay.portal.model.Account account = null;
      com.liferay.portal.model.User user = null;
      com.liferay.portal.model.User realUser = null;
      com.liferay.portal.model.Contact contact = null;
      com.liferay.portal.model.Layout layout = null;
      java.util.List layouts = null;
      java.lang.Long plid = null;
      com.liferay.portal.model.LayoutTypePortlet layoutTypePortlet = null;
      java.lang.Long scopeGroupId = null;
      com.liferay.portal.security.permission.PermissionChecker permissionChecker = null;
      java.util.Locale locale = null;
      java.util.TimeZone timeZone = null;
      com.liferay.portal.model.Theme theme = null;
      com.liferay.portal.model.ColorScheme colorScheme = null;
      com.liferay.portal.theme.PortletDisplay portletDisplay = null;
      java.lang.Long portletGroupId = null;
      themeDisplay = (com.liferay.portal.theme.ThemeDisplay) _jspx_page_context.findAttribute("themeDisplay");
      company = (com.liferay.portal.model.Company) _jspx_page_context.findAttribute("company");
      account = (com.liferay.portal.model.Account) _jspx_page_context.findAttribute("account");
      user = (com.liferay.portal.model.User) _jspx_page_context.findAttribute("user");
      realUser = (com.liferay.portal.model.User) _jspx_page_context.findAttribute("realUser");
      contact = (com.liferay.portal.model.Contact) _jspx_page_context.findAttribute("contact");
      layout = (com.liferay.portal.model.Layout) _jspx_page_context.findAttribute("layout");
      layouts = (java.util.List) _jspx_page_context.findAttribute("layouts");
      plid = (java.lang.Long) _jspx_page_context.findAttribute("plid");
      layoutTypePortlet = (com.liferay.portal.model.LayoutTypePortlet) _jspx_page_context.findAttribute("layoutTypePortlet");
      scopeGroupId = (java.lang.Long) _jspx_page_context.findAttribute("scopeGroupId");
      permissionChecker = (com.liferay.portal.security.permission.PermissionChecker) _jspx_page_context.findAttribute("permissionChecker");
      locale = (java.util.Locale) _jspx_page_context.findAttribute("locale");
      timeZone = (java.util.TimeZone) _jspx_page_context.findAttribute("timeZone");
      theme = (com.liferay.portal.model.Theme) _jspx_page_context.findAttribute("theme");
      colorScheme = (com.liferay.portal.model.ColorScheme) _jspx_page_context.findAttribute("colorScheme");
      portletDisplay = (com.liferay.portal.theme.PortletDisplay) _jspx_page_context.findAttribute("portletDisplay");
      portletGroupId = (java.lang.Long) _jspx_page_context.findAttribute("portletGroupId");
      out.write('\n');
      out.write('\n');

/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

      out.write('\n');
      out.write('\n');
      //  portlet:defineObjects
      com.liferay.taglib.portlet.DefineObjectsTag _jspx_th_portlet_005fdefineObjects_005f0 = (com.liferay.taglib.portlet.DefineObjectsTag) _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.get(com.liferay.taglib.portlet.DefineObjectsTag.class);
      _jspx_th_portlet_005fdefineObjects_005f0.setPageContext(_jspx_page_context);
      _jspx_th_portlet_005fdefineObjects_005f0.setParent(null);
      int _jspx_eval_portlet_005fdefineObjects_005f0 = _jspx_th_portlet_005fdefineObjects_005f0.doStartTag();
      if (_jspx_th_portlet_005fdefineObjects_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.reuse(_jspx_th_portlet_005fdefineObjects_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fportlet_005fdefineObjects_005fnobody.reuse(_jspx_th_portlet_005fdefineObjects_005f0);
      javax.portlet.ActionRequest actionRequest = null;
      javax.portlet.ActionResponse actionResponse = null;
      javax.portlet.EventRequest eventRequest = null;
      javax.portlet.EventResponse eventResponse = null;
      javax.portlet.PortletConfig portletConfig = null;
      java.lang.String portletName = null;
      javax.portlet.PortletPreferences portletPreferences = null;
      java.util.Map portletPreferencesValues = null;
      javax.portlet.PortletSession portletSession = null;
      java.util.Map portletSessionScope = null;
      javax.portlet.RenderRequest renderRequest = null;
      javax.portlet.RenderResponse renderResponse = null;
      javax.portlet.ResourceRequest resourceRequest = null;
      javax.portlet.ResourceResponse resourceResponse = null;
      actionRequest = (javax.portlet.ActionRequest) _jspx_page_context.findAttribute("actionRequest");
      actionResponse = (javax.portlet.ActionResponse) _jspx_page_context.findAttribute("actionResponse");
      eventRequest = (javax.portlet.EventRequest) _jspx_page_context.findAttribute("eventRequest");
      eventResponse = (javax.portlet.EventResponse) _jspx_page_context.findAttribute("eventResponse");
      portletConfig = (javax.portlet.PortletConfig) _jspx_page_context.findAttribute("portletConfig");
      portletName = (java.lang.String) _jspx_page_context.findAttribute("portletName");
      portletPreferences = (javax.portlet.PortletPreferences) _jspx_page_context.findAttribute("portletPreferences");
      portletPreferencesValues = (java.util.Map) _jspx_page_context.findAttribute("portletPreferencesValues");
      portletSession = (javax.portlet.PortletSession) _jspx_page_context.findAttribute("portletSession");
      portletSessionScope = (java.util.Map) _jspx_page_context.findAttribute("portletSessionScope");
      renderRequest = (javax.portlet.RenderRequest) _jspx_page_context.findAttribute("renderRequest");
      renderResponse = (javax.portlet.RenderResponse) _jspx_page_context.findAttribute("renderResponse");
      resourceRequest = (javax.portlet.ResourceRequest) _jspx_page_context.findAttribute("resourceRequest");
      resourceResponse = (javax.portlet.ResourceResponse) _jspx_page_context.findAttribute("resourceResponse");
      out.write('\n');
      out.write('\n');

WindowState windowState = null;
PortletMode portletMode = null;

PortletURL currentURLObj = null;

if (renderRequest != null) {
	windowState = renderRequest.getWindowState();
	portletMode = renderRequest.getPortletMode();

	currentURLObj = PortletURLUtil.getCurrent(renderRequest, renderResponse);
}
else if (resourceRequest != null) {
	windowState = resourceRequest.getWindowState();
	portletMode = resourceRequest.getPortletMode();

	currentURLObj = PortletURLUtil.getCurrent(resourceRequest, resourceResponse);
}

String currentURL = currentURLObj.toString();
//String currentURL = PortalUtil.getCurrentURL(request);

      out.write('\n');
      out.write('\n');

/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

PortalPreferences portalPrefs = PortletPreferencesFactoryUtil.getPortalPreferences(request);

Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);

      out.write('\n');
      out.write('\n');

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

JournalArticle article = (JournalArticle)row.getObject();

      out.write('\n');
      out.write('\n');
      //  liferay-ui:icon-menu
      com.liferay.taglib.ui.IconMenuTag _jspx_th_liferay_002dui_005ficon_002dmenu_005f0 = (com.liferay.taglib.ui.IconMenuTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu.get(com.liferay.taglib.ui.IconMenuTag.class);
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setPageContext(_jspx_page_context);
      _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setParent(null);
      int _jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 = _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doStartTag();
      if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doInitBody();
        }
        do {
          out.write('\n');
          out.write('	');
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
          // /html/portlet/journal/article_action.jsp(26,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f0.setTest( JournalArticlePermission.contains(permissionChecker, article, ActionKeys.UPDATE) );
          int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
          if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  portlet:renderURL
              com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f0 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
              _jspx_th_portlet_005frenderURL_005f0.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005frenderURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
              // /html/portlet/journal/article_action.jsp(27,2) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005frenderURL_005f0.setVar("editURL");
              int _jspx_eval_portlet_005frenderURL_005f0 = _jspx_th_portlet_005frenderURL_005f0.doStartTag();
              if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_portlet_005frenderURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_portlet_005frenderURL_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_portlet_005fparam_005f0(_jspx_th_portlet_005frenderURL_005f0, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f1 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                  // /html/portlet/journal/article_action.jsp(29,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f1.setName("redirect");
                  // /html/portlet/journal/article_action.jsp(29,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f1.setValue( currentURL );
                  int _jspx_eval_portlet_005fparam_005f1 = _jspx_th_portlet_005fparam_005f1.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f1);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f2 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f2.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                  // /html/portlet/journal/article_action.jsp(30,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f2.setName("groupId");
                  // /html/portlet/journal/article_action.jsp(30,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f2.setValue( String.valueOf(article.getGroupId()) );
                  int _jspx_eval_portlet_005fparam_005f2 = _jspx_th_portlet_005fparam_005f2.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f2);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f3 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                  // /html/portlet/journal/article_action.jsp(31,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f3.setName("articleId");
                  // /html/portlet/journal/article_action.jsp(31,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f3.setValue( article.getArticleId() );
                  int _jspx_eval_portlet_005fparam_005f3 = _jspx_th_portlet_005fparam_005f3.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f3);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f4 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f4.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
                  // /html/portlet/journal/article_action.jsp(32,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f4.setName("version");
                  // /html/portlet/journal/article_action.jsp(32,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f4.setValue( String.valueOf(article.getVersion()) );
                  int _jspx_eval_portlet_005fparam_005f4 = _jspx_th_portlet_005fparam_005f4.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f4);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_portlet_005frenderURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_portlet_005frenderURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f0);
              java.lang.String editURL = null;
              editURL = (java.lang.String) _jspx_page_context.findAttribute("editURL");
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  liferay-ui:icon
              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f0 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
              _jspx_th_liferay_002dui_005ficon_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ficon_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
              // /html/portlet/journal/article_action.jsp(35,2) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f0.setImage("edit");
              // /html/portlet/journal/article_action.jsp(35,2) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f0.setUrl( editURL );
              int _jspx_eval_liferay_002dui_005ficon_005f0 = _jspx_th_liferay_002dui_005ficon_005f0.doStartTag();
              if (_jspx_th_liferay_002dui_005ficon_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f0);
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_c_005fif_005f0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
          // /html/portlet/journal/article_action.jsp(41,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f1.setTest( JournalArticlePermission.contains(permissionChecker, article, ActionKeys.PERMISSIONS) );
          int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
          if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  liferay-security:permissionsURL
              com.liferay.taglib.security.PermissionsURLTag _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0 = (com.liferay.taglib.security.PermissionsURLTag) _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.get(com.liferay.taglib.security.PermissionsURLTag.class);
              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
              // /html/portlet/journal/article_action.jsp(42,2) name = modelResource type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setModelResource( JournalArticle.class.getName() );
              // /html/portlet/journal/article_action.jsp(42,2) name = modelResourceDescription type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setModelResourceDescription( article.getTitle() );
              // /html/portlet/journal/article_action.jsp(42,2) name = resourcePrimKey type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setResourcePrimKey( String.valueOf(article.getResourcePrimKey()) );
              // /html/portlet/journal/article_action.jsp(42,2) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.setVar("permissionsURL");
              int _jspx_eval_liferay_002dsecurity_005fpermissionsURL_005f0 = _jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.doStartTag();
              if (_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.reuse(_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dsecurity_005fpermissionsURL_0026_005fvar_005fresourcePrimKey_005fmodelResourceDescription_005fmodelResource_005fnobody.reuse(_jspx_th_liferay_002dsecurity_005fpermissionsURL_005f0);
              java.lang.String permissionsURL = null;
              permissionsURL = (java.lang.String) _jspx_page_context.findAttribute("permissionsURL");
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  liferay-ui:icon
              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f1 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
              _jspx_th_liferay_002dui_005ficon_005f1.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ficon_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f1);
              // /html/portlet/journal/article_action.jsp(49,2) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f1.setImage("permissions");
              // /html/portlet/journal/article_action.jsp(49,2) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f1.setUrl( permissionsURL );
              int _jspx_eval_liferay_002dui_005ficon_005f1 = _jspx_th_liferay_002dui_005ficon_005f1.doStartTag();
              if (_jspx_th_liferay_002dui_005ficon_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f1);
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_c_005fif_005f1.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f2.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
          // /html/portlet/journal/article_action.jsp(55,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f2.setTest( JournalArticlePermission.contains(permissionChecker, article, ActionKeys.VIEW) );
          int _jspx_eval_c_005fif_005f2 = _jspx_th_c_005fif_005f2.doStartTag();
          if (_jspx_eval_c_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\n");
              out.write("\n");
              out.write("\t\t");

		StringBundler sb = new StringBundler(7);

		sb.append(themeDisplay.getPathMain());
		sb.append("/journal/view_article_content?groupId=");
		sb.append(article.getGroupId());
		sb.append("&articleId=");
		sb.append(article.getArticleId());
		sb.append("&version=");
		sb.append(article.getVersion());
		
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  liferay-ui:icon
              com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f2 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005ftarget_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
              _jspx_th_liferay_002dui_005ficon_005f2.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ficon_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/portlet/journal/article_action.jsp(69,2) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f2.setImage("preview");
              // /html/portlet/journal/article_action.jsp(69,2) name = target type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f2.setTarget("_blank");
              // /html/portlet/journal/article_action.jsp(69,2) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_005f2.setUrl( sb.toString() );
              int _jspx_eval_liferay_002dui_005ficon_005f2 = _jspx_th_liferay_002dui_005ficon_005f2.doStartTag();
              if (_jspx_th_liferay_002dui_005ficon_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005ftarget_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005ftarget_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f2);
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  c:if
              org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
              _jspx_th_c_005fif_005f3.setPageContext(_jspx_page_context);
              _jspx_th_c_005fif_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f2);
              // /html/portlet/journal/article_action.jsp(75,2) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_c_005fif_005f3.setTest( JournalPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_ARTICLE) );
              int _jspx_eval_c_005fif_005f3 = _jspx_th_c_005fif_005f3.doStartTag();
              if (_jspx_eval_c_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:renderURL
                  com.liferay.taglib.portlet.RenderURLTag _jspx_th_portlet_005frenderURL_005f1 = (com.liferay.taglib.portlet.RenderURLTag) _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.get(com.liferay.taglib.portlet.RenderURLTag.class);
                  _jspx_th_portlet_005frenderURL_005f1.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005frenderURL_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                  // /html/portlet/journal/article_action.jsp(76,3) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005frenderURL_005f1.setVar("copyURL");
                  int _jspx_eval_portlet_005frenderURL_005f1 = _jspx_th_portlet_005frenderURL_005f1.doStartTag();
                  if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                    if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.pushBody();
                      _jspx_th_portlet_005frenderURL_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                      _jspx_th_portlet_005frenderURL_005f1.doInitBody();
                    }
                    do {
                      out.write("\n");
                      out.write("\t\t\t\t");
                      if (_jspx_meth_portlet_005fparam_005f5(_jspx_th_portlet_005frenderURL_005f1, _jspx_page_context))
                        return;
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f6 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f6.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                      // /html/portlet/journal/article_action.jsp(78,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f6.setName("redirect");
                      // /html/portlet/journal/article_action.jsp(78,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f6.setValue( currentURL );
                      int _jspx_eval_portlet_005fparam_005f6 = _jspx_th_portlet_005fparam_005f6.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f6);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f7 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f7.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                      // /html/portlet/journal/article_action.jsp(79,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f7.setName("groupId");
                      // /html/portlet/journal/article_action.jsp(79,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f7.setValue( String.valueOf(article.getGroupId()) );
                      int _jspx_eval_portlet_005fparam_005f7 = _jspx_th_portlet_005fparam_005f7.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f7);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f8 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f8.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                      // /html/portlet/journal/article_action.jsp(80,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f8.setName("oldArticleId");
                      // /html/portlet/journal/article_action.jsp(80,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f8.setValue( article.getArticleId() );
                      int _jspx_eval_portlet_005fparam_005f8 = _jspx_th_portlet_005fparam_005f8.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f8);
                      out.write("\n");
                      out.write("\t\t\t\t");
                      //  portlet:param
                      com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f9 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                      _jspx_th_portlet_005fparam_005f9.setPageContext(_jspx_page_context);
                      _jspx_th_portlet_005fparam_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
                      // /html/portlet/journal/article_action.jsp(81,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f9.setName("version");
                      // /html/portlet/journal/article_action.jsp(81,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                      _jspx_th_portlet_005fparam_005f9.setValue( String.valueOf(article.getVersion()) );
                      int _jspx_eval_portlet_005fparam_005f9 = _jspx_th_portlet_005fparam_005f9.doStartTag();
                      if (_jspx_th_portlet_005fparam_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                        return;
                      }
                      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f9);
                      out.write("\n");
                      out.write("\t\t\t");
                      int evalDoAfterBody = _jspx_th_portlet_005frenderURL_005f1.doAfterBody();
                      if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                        break;
                    } while (true);
                    if (_jspx_eval_portlet_005frenderURL_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                      out = _jspx_page_context.popBody();
                    }
                  }
                  if (_jspx_th_portlet_005frenderURL_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f1);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005frenderURL_0026_005fvar.reuse(_jspx_th_portlet_005frenderURL_005f1);
                  java.lang.String copyURL = null;
                  copyURL = (java.lang.String) _jspx_page_context.findAttribute("copyURL");
                  out.write("\n");
                  out.write("\n");
                  out.write("\t\t\t");
                  //  liferay-ui:icon
                  com.liferay.taglib.ui.IconTag _jspx_th_liferay_002dui_005ficon_005f3 = (com.liferay.taglib.ui.IconTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.get(com.liferay.taglib.ui.IconTag.class);
                  _jspx_th_liferay_002dui_005ficon_005f3.setPageContext(_jspx_page_context);
                  _jspx_th_liferay_002dui_005ficon_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f3);
                  // /html/portlet/journal/article_action.jsp(84,3) name = image type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f3.setImage("copy");
                  // /html/portlet/journal/article_action.jsp(84,3) name = url type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_liferay_002dui_005ficon_005f3.setUrl( copyURL.toString() );
                  int _jspx_eval_liferay_002dui_005ficon_005f3 = _jspx_th_liferay_002dui_005ficon_005f3.doStartTag();
                  if (_jspx_th_liferay_002dui_005ficon_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                    return;
                  }
                  _005fjspx_005ftagPool_005fliferay_002dui_005ficon_0026_005furl_005fimage_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_005f3);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_c_005fif_005f3.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
              }
              if (_jspx_th_c_005fif_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
                return;
              }
              _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f3);
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_c_005fif_005f2.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f2);
          out.write('\n');
          out.write('\n');
          out.write('	');
          //  c:if
          org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f4 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
          _jspx_th_c_005fif_005f4.setPageContext(_jspx_page_context);
          _jspx_th_c_005fif_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
          // /html/portlet/journal/article_action.jsp(91,1) name = test type = boolean reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_c_005fif_005f4.setTest( JournalArticlePermission.contains(permissionChecker, article, ActionKeys.DELETE) );
          int _jspx_eval_c_005fif_005f4 = _jspx_th_c_005fif_005f4.doStartTag();
          if (_jspx_eval_c_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write('\n');
              out.write('	');
              out.write('	');
              //  portlet:actionURL
              com.liferay.taglib.portlet.ActionURLTag _jspx_th_portlet_005factionURL_005f0 = (com.liferay.taglib.portlet.ActionURLTag) _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.get(com.liferay.taglib.portlet.ActionURLTag.class);
              _jspx_th_portlet_005factionURL_005f0.setPageContext(_jspx_page_context);
              _jspx_th_portlet_005factionURL_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
              // /html/portlet/journal/article_action.jsp(92,2) name = var type = null reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_portlet_005factionURL_005f0.setVar("deleteURL");
              int _jspx_eval_portlet_005factionURL_005f0 = _jspx_th_portlet_005factionURL_005f0.doStartTag();
              if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_portlet_005factionURL_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_portlet_005factionURL_005f0.doInitBody();
                }
                do {
                  out.write("\n");
                  out.write("\t\t\t");
                  if (_jspx_meth_portlet_005fparam_005f10(_jspx_th_portlet_005factionURL_005f0, _jspx_page_context))
                    return;
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f11 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f11.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                  // /html/portlet/journal/article_action.jsp(94,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f11.setName( Constants.CMD );
                  // /html/portlet/journal/article_action.jsp(94,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f11.setValue( Constants.DELETE );
                  int _jspx_eval_portlet_005fparam_005f11 = _jspx_th_portlet_005fparam_005f11.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f11);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f12 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f12.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                  // /html/portlet/journal/article_action.jsp(95,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f12.setName("redirect");
                  // /html/portlet/journal/article_action.jsp(95,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f12.setValue( currentURL );
                  int _jspx_eval_portlet_005fparam_005f12 = _jspx_th_portlet_005fparam_005f12.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f12);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f13 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f13.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                  // /html/portlet/journal/article_action.jsp(96,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f13.setName("groupId");
                  // /html/portlet/journal/article_action.jsp(96,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f13.setValue( String.valueOf(article.getGroupId()) );
                  int _jspx_eval_portlet_005fparam_005f13 = _jspx_th_portlet_005fparam_005f13.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f13);
                  out.write("\n");
                  out.write("\t\t\t");
                  //  portlet:param
                  com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f14 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
                  _jspx_th_portlet_005fparam_005f14.setPageContext(_jspx_page_context);
                  _jspx_th_portlet_005fparam_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
                  // /html/portlet/journal/article_action.jsp(97,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f14.setName("deleteArticleIds");
                  // /html/portlet/journal/article_action.jsp(97,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
                  _jspx_th_portlet_005fparam_005f14.setValue( article.getArticleId() );
                  int _jspx_eval_portlet_005fparam_005f14 = _jspx_th_portlet_005fparam_005f14.doStartTag();
                  if (_jspx_th_portlet_005fparam_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
                    return;
                  }
                  _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f14);
                  out.write('\n');
                  out.write('	');
                  out.write('	');
                  int evalDoAfterBody = _jspx_th_portlet_005factionURL_005f0.doAfterBody();
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_portlet_005factionURL_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.popBody();
                }
              }
              if (_jspx_th_portlet_005factionURL_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.reuse(_jspx_th_portlet_005factionURL_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fportlet_005factionURL_0026_005fvar.reuse(_jspx_th_portlet_005factionURL_005f0);
              java.lang.String deleteURL = null;
              deleteURL = (java.lang.String) _jspx_page_context.findAttribute("deleteURL");
              out.write("\n");
              out.write("\n");
              out.write("\t\t");
              //  liferay-ui:icon-delete
              com.liferay.taglib.ui.IconDeleteTag _jspx_th_liferay_002dui_005ficon_002ddelete_005f0 = (com.liferay.taglib.ui.IconDeleteTag) _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.get(com.liferay.taglib.ui.IconDeleteTag.class);
              _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setPageContext(_jspx_page_context);
              _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f4);
              // /html/portlet/journal/article_action.jsp(100,2) name = url type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
              _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.setUrl( deleteURL );
              int _jspx_eval_liferay_002dui_005ficon_002ddelete_005f0 = _jspx_th_liferay_002dui_005ficon_002ddelete_005f0.doStartTag();
              if (_jspx_th_liferay_002dui_005ficon_002ddelete_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f0);
                return;
              }
              _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002ddelete_0026_005furl_005fnobody.reuse(_jspx_th_liferay_002dui_005ficon_002ddelete_005f0);
              out.write('\n');
              out.write('	');
              int evalDoAfterBody = _jspx_th_c_005fif_005f4.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_c_005fif_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
            return;
          }
          _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f4);
          out.write('\n');
          int evalDoAfterBody = _jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_liferay_002dui_005ficon_002dmenu_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_liferay_002dui_005ficon_002dmenu_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu.reuse(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fliferay_002dui_005ficon_002dmenu.reuse(_jspx_th_liferay_002dui_005ficon_002dmenu_005f0);
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_portlet_005fparam_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f0 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f0.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f0);
    // /html/portlet/journal/article_action.jsp(28,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setName("struts_action");
    // /html/portlet/journal/article_action.jsp(28,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f0.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f0 = _jspx_th_portlet_005fparam_005f0.doStartTag();
    if (_jspx_th_portlet_005fparam_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f0);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005frenderURL_005f1, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f5 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f5.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005frenderURL_005f1);
    // /html/portlet/journal/article_action.jsp(77,4) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f5.setName("struts_action");
    // /html/portlet/journal/article_action.jsp(77,4) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f5.setValue("/journal/copy_article");
    int _jspx_eval_portlet_005fparam_005f5 = _jspx_th_portlet_005fparam_005f5.doStartTag();
    if (_jspx_th_portlet_005fparam_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f5);
    return false;
  }

  private boolean _jspx_meth_portlet_005fparam_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_portlet_005factionURL_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  portlet:param
    com.liferay.taglib.util.ParamTag _jspx_th_portlet_005fparam_005f10 = (com.liferay.taglib.util.ParamTag) _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.get(com.liferay.taglib.util.ParamTag.class);
    _jspx_th_portlet_005fparam_005f10.setPageContext(_jspx_page_context);
    _jspx_th_portlet_005fparam_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_portlet_005factionURL_005f0);
    // /html/portlet/journal/article_action.jsp(93,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f10.setName("struts_action");
    // /html/portlet/journal/article_action.jsp(93,3) name = value type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_portlet_005fparam_005f10.setValue("/journal/edit_article");
    int _jspx_eval_portlet_005fparam_005f10 = _jspx_th_portlet_005fparam_005f10.doStartTag();
    if (_jspx_th_portlet_005fparam_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fportlet_005fparam_0026_005fvalue_005fname_005fnobody.reuse(_jspx_th_portlet_005fparam_005f10);
    return false;
  }
}