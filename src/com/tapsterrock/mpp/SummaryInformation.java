/*
 * file:       SummaryInformation.java
 * author:     Jon Iles
 * copyright:  (c) Tapster Rock Limited 2004
 * date:       Dec 2, 2004
 */
 
package com.tapsterrock.mpp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hpsf.Property;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.Section;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;

import com.tapsterrock.mpx.MPXException;

/**
 * This class encapsulates the functionlaity required to retrieve document 
 * summary information from MPP files. This code is common to both the
 * MPP8 and MPP9 file formats.
 */
final class SummaryInformation
{
   /**
    * Constructor.
    * 
    * @param rootDir root of the POI file system
    * @throws MPXException
    */
   public SummaryInformation (DirectoryEntry rootDir)
      throws MPXException
   {
      try
      {
         PropertySet ps = new PropertySet(new DocumentInputStream (((DocumentEntry)rootDir.getEntry("\005SummaryInformation"))));
         HashMap map = getPropertyMap(ps);
 
         m_projectTitle = (String)map.get(PROJECT_TITLE);
         m_subject = (String)map.get(SUBJECT);
         m_author = (String)map.get(AUTHOR);
         m_keywords = (String)map.get(KEYWORDS);
         m_comments = (String)map.get(COMMENTS);
         
         ps = new PropertySet(new DocumentInputStream (((DocumentEntry)rootDir.getEntry("\005DocumentSummaryInformation"))));         
         map = getPropertyMap(ps);
         m_company = (String)map.get(COMPANY);
         m_manager = (String)map.get(MANAGER);               
      }
      
      catch (Exception ex)
      {
         throw new MPXException (MPXException.READ_ERROR, ex);        
      }
   }
   
   /**
    * This method reads the contents of a property set and returns
    * a map relating property IDs and values together.
    * 
    * @param ps property set
    * @return map
    */
   private HashMap getPropertyMap (PropertySet ps)   
   {
      HashMap map = new HashMap();
      Property[] properties;
      Property property;
      List sections = ps.getSections();
      Iterator iter = sections.iterator();
      Section section;
      int index = 100;
      
      while (iter.hasNext() == true)
      {
         section = (Section)iter.next();
         properties = section.getProperties();
         for (int loop=0; loop < properties.length; loop++)
         {
           property = properties[loop];
           map.put(new Integer(index+property.getID()), property.getValue());
           //System.out.println ("id="+(index+property.getID())+" value="+property.getValue());
         }
         index += 100;
      }
      return (map);
   }

   /**
    * Retrieve the author's name
    * 
    * @return author's name
    */
   public String getAuthor()
   {
      return (m_author);
   }
   
   /**
    * Retrieve comments.
    * 
    * @return comments
    */
   public String getComments()
   {
      return (m_comments);
   }
   
   /**
    * Retrieve the company name.
    * 
    * @return company name
    */
   public String getCompany()
   {
      return (m_company);
   }
   
   /**
    * Retrieve the keywords.
    * 
    * @return keywords
    */
   public String getKeywords()
   {
      return (m_keywords);
   }
   
   /**
    * Retrieve the manager.
    * 
    * @return manager
    */
   public String getManager()
   {
      return (m_manager);
   }
   
   /**
    * Retrieve the project title.
    * 
    * @return project title
    */
   public String getProjectTitle()
   {
      return (m_projectTitle);
   }
   
   /**
    * Retrieve the subject.
    * 
    * @return subject
    */
   public String getSubject()
   {
      return (m_subject);
   }
   
   private String m_projectTitle;
   private String m_subject;
   private String m_author;
   private String m_keywords;
   private String m_comments;
   private String m_manager;
   private String m_company;
   
   /**
    * Constants representing properties
    */
   private static final Integer PROJECT_TITLE = new Integer (102);   
   private static final Integer SUBJECT = new Integer (103);   
   private static final Integer AUTHOR = new Integer (104);   
   private static final Integer KEYWORDS = new Integer (105);   
   private static final Integer COMMENTS = new Integer (106);      
   private static final Integer MANAGER = new Integer (114);   
   private static final Integer COMPANY = new Integer (115);   
}