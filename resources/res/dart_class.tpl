
<%
out.println("import \'"+filepath.replace("/lib","..")+"\';");

for(int i=0;i<classes.size();i++)
{
    MClass mclass = classes.get(i);

%>
/*
${mclass.name}
*/
class R${mclass.name} {

    static void setValue(${mclass.name} obj,String field,Object value){

       switch(field){
<%
       for(MField field : mclass.fields){

            if( "List".equals(field.type) ){

                out.println '            case \"'+field.name+'\":  obj.'+field.name+'= new List<'+field.typeT+'>.from(value);break;';

            }
            else
                out.println '            case \"'+field.name+'\":  obj.'+field.name+'=value as '+field.type+';break;'


       }
            out.println '            default: new Exception(\"not found field:$field\");';
%>
       }

    }

    static Object getValue(${mclass.name} obj,String field){

        switch(field){

<%
       for(MField field : mclass.fields){
            out.println '            case \"'+field.name+'\": return obj.'+field.name+';';
       }
            out.println '            default: new Exception(\"not found field:$field\");';
%>
        }


    }

    static  int getFieldCount(){

        return <%=mclass.fields.size()%>;

    }

    static String getField(int index){

        switch(index){

<%
       int i_f=0;
       for(MField field : mclass.fields){
            out.println '            case '+i_f+': return \"'+field.name+'\";';
            i_f++;
       }
            out.println '            default: new Exception(\"not found field:$index\");';
%>

        }

    }

    static Type getFieldType(int index){

        switch(index){

<%
       int i_k=0;
       for(MField field : mclass.fields){
            out.println '            case '+i_k+': return '+field.type+';';
            i_k++;
       }
            out.println '            default: new Exception(\"not found field:$index\");';
%>


        }

    }

    static Type getFieldT(int index){

        switch(index){

<%
       int i_t=0;
       for(MField field : mclass.fields){
            out.println '            case '+i_t+': return '+field.typeT+';';
            i_t++;
       }
            out.println '            default: new Exception(\"not found field t:$index\");';
%>


        }

    }




}
<%
}
%>