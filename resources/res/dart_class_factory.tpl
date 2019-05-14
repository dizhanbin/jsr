
import 'dart:convert';

<%
    for(String rmdart : files){

        String filename = new File(rmdart).getName();
        out.println("import \'R_"+filename+"\';");
        out.println("import \'"+rmdart.replace("/lib","..")+"\';");

    }

%>


class Class{


  static Object getValue(Object value,String field){

    String classname = value.runtimeType.toString();
    switch(classname){

<%
  for(String rmdart : classes){

          out.println('      case \"'+rmdart+'\": return R'+rmdart+'.getValue(value as '+rmdart+',field);');

  }

%>


      default: new Exception("not implement \$classname");

    }


  }


  static void setValue(Object obj,String field,Object value){

    String classname = obj.runtimeType.toString();
    switch(classname){

<%
  for(String rmdart : classes){

          out.println('      case \"'+rmdart+'\":  R'+rmdart+'.setValue(obj as '+rmdart+',field,value);break;');

  }

%>

      default: new Exception("not implement \$classname");
    }

  }

  static int getFieldCount(Type type){

    String classname = type.toString();
    switch(classname){
<%
  for(String rmdart : classes){



          out.println('      case \"'+rmdart+'\": return R'+rmdart+'.getFieldCount();');

  }

%>
      default: new Exception("not implement \$classname");
    }

  }

  static String getFieldName(Type type,int index){

    String classname = type.toString();
    switch(classname){

<%
  for(String rmdart : classes){



          out.println('      case \"'+rmdart+'\": return R'+rmdart+'.getField(index);');

  }

%>

      default: new Exception("not implement \$classname");
    }

  }

 static Type getFieldT(Type type,int index){

    String classname = type.toString();
    switch(classname){

<%
  for(String rmdart : classes){

          out.println('      case \"'+rmdart+'\": return R'+rmdart+'.getFieldT(index);');

  }

%>

      default: new Exception("not implement \$classname");
    }

  }


 static Object createFormName(String classname){

    switch(classname){
<%
  for(String rmdart : classes){


          out.println('      case \"'+rmdart+'\": return '+rmdart+'();');

  }

  out.println('      default: new Exception(\"not implement \$classname\");');
%>

    }

  }
   static Object fromJsonStr(Type type,String jsonstr){

    Map map = json.decode(jsonstr);
    return fromJson(type,map);

  }

  static Object fromJson(Type type,Map<String,dynamic> json){

      Object obj = createFormName(type.toString());

      for(int i=0;i<getFieldCount(type);i++){

        String fieldname = getFieldName(type, i);

        if( json.containsKey(fieldname)  && json[fieldname] != null ){

              Type fieldtype = getFieldT(type, i);
              if( fieldtype != null ){

                List list = json[fieldname] as List;
                List values = [];
                for(int i=0;i<list.length;i++){
                    values.add( fromJson(fieldtype, list[i]) );
                }
                setValue(obj, fieldname, values);

              }
              else
                setValue(obj, fieldname, json[fieldname]);

        }


      }
      return obj;

  }

   static String toJson(Object value){

       return json.encode(value);

   }

    static Type getFieldType(Type type,int index){

        switch(type)
        {

<%
  for(String rmdart : classes){



          out.println('         case '+rmdart+': return R'+rmdart+'.getFieldType(index);');

  }
  out.println('         default: new Exception(\"not implement $type\");')

%>

        }

    }



    static Map<String, String> getFieldsByType(Type table) {
        // TODO: implement getFields
        Map<String,String> fields = new Map();

        for(int i=0;i<Class.getFieldCount(table);i++){

          fields[Class.getFieldName(table, i)] = Class.getFieldType(table,i).toString();

        }
        return fields;

     }



    static Map<String, String> getFields(String table) {

        switch(table){
 <%
   for(String rmdart : classes){


           out.println('            case \"'+rmdart+'\": return getFieldsByType('+rmdart+');');

   }
           out.println('            default: new Exception(\"not implement $table\");');
 %>
        }

    }

}