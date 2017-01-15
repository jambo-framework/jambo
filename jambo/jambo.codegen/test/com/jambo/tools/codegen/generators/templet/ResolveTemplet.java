package com.jambo.tools.codegen.generators.templet;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.jambo.tools.codegen.util.FieldsHelper;
import com.jambo.tools.codegen.util.ValueStore;
import com.jambo.tools.codegen.wizards.params.FieldList;

public class ResolveTemplet {
	public void setInitParamsTo() {
        VelocityContext context = new VelocityContext();
        context.put("", "") ;
	}
	
	public ResolveTemplet(String templateFile) throws Exception{

        try
        {
//            File file1 = new File("E:\\test\\test\\src","com\\test") ;
//            file1.mkdirs() ;
//            File file = new File(file1,"list.jsp") ;
            /*
             * setup
             */

            /*
             *  Make a context object and populate with the data.  This
             *  is where the Velocity engine gets the data to resolve the
             *  references (ex. $list) in the template
             */
            FieldList fieldList = new FieldList();
            fieldList.addField("a", 1, String.class, 0) ;
            fieldList.addField("b", 1, String.class, 0) ;
            ValueStore.fields = fieldList.getFields() ;
            FieldsHelper helper = new FieldsHelper(ValueStore.fields);
            ValueStore.pkFields = "a,b,c" ;
            
            VelocityContext context = new VelocityContext();
//            context.put("list", getNames());
//            context.put("isPkg", true) ;
            context.put("pkgName", "com.steven.aaa") ;
            context.put("modelPkg", "com.steven.po") ;
            context.put("poClassName", "TestPO") ;
            context.put("className", "TestClass") ;
            context.put("author", null) ;
            context.put("ELStart", "${") ;
            context.put("Eend", "}") ;
            context.put("EEStart", "#{") ;

    		context.put("includeFile", "aa") ;
    		context.put("helper", helper) ;
    		context.put("moduleName", "Ma") ;
    		context.put("baseClassName", "Ba") ;
    		
            String[] s = {"1","2","3"} ;
            List list = new ArrayList();
            Collections.addAll(list, s) ;
            context.put("array", list) ;
            
            /*
             *  get the Template object.  This is the parsed version of your
             *  template input file.  Note that getTemplate() can throw
             *   ResourceNotFoundException : if it doesn't find the template
             *   ParseErrorException : if there is something wrong with the VTL
             *   Exception : if something else goes wrong (this is generally
             *        indicative of as serious problem...)
             */

            Template template =  null;

            try
            {
            	String root = new java.io.File("").getAbsolutePath() ;
            	String apath = this.getClass().getResource(templateFile).getPath() ;
            	System.out.println(apath) ;
            	if (apath.startsWith("/")){
            		apath = apath.substring(1) ;
            	}
            	System.out.println(apath) ;
            	apath = apath.substring(root.length()) ;
            	if (apath.startsWith("/")){
            		apath = apath.substring(1) ;
            	}
            	
//            	String f = "src/com/maywide/tools/codegen/generators/templet/" + templateFile ;
            	System.out.println(apath) ;
                template = Velocity.getTemplate(apath);
            }
            catch( ResourceNotFoundException rnfe )
            {
                System.out.println("Example : error : cannot find template " + templateFile );
                throw rnfe ;
            }
            catch( ParseErrorException pee )
            {
                System.out.println("Example : Syntax error in template " + templateFile + ":" + pee );
            }

            /*
             *  Now have the template engine process your template using the
             *  data placed into the context.  Think of it as a  'merge'
             *  of the template and the data to produce the output stream.
             */

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(System.out));

            if ( template != null)
                template.merge(context, writer);

            /*
             *  flush and cleanup
             */

            writer.flush();
            writer.close();
        }
        catch( Exception e )
        {
        	throw e ;
        }
	}
	
	public static void main(String[] args) throws Exception{
		new ResolveTemplet("Struts2FormTemplet.vm") ;
	}
}