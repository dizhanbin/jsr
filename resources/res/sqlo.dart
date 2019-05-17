import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';
import 'Class.dart';

class SQLO {
  static Database database;
  static String tables;
  static int seq = 0;

  static final String keyid = "keyid";

  static  Future<void> INIT() async {
    var databasesPath = await getDatabasesPath();
    String path = join(databasesPath, 'sqlo.db');
    //await deleteDatabase(path);
    database = await openDatabase(path, version: 1,
        onCreate: (Database db, int version) async {
      //await db.execute('CREATE TABLE Test (id INTEGER PRIMARY KEY, name TEXT, value INTEGER, num REAL)');
    });

    return initTables();

  }


  static Future<void> initTables() async {

    List<Map> list = await database.rawQuery('SELECT name FROM sqlite_master');

    tables = ",";
    for (Map map in list) {
      tables += map['name'] + ",";
    }
    print("tables:$tables");

    return Future.value();

  }

  static String sequence() {
    if (seq == 0) {
      seq = DateTime.now().millisecondsSinceEpoch;
    }
    seq++;

    return seq.toString();
  }

  static String getTable(Type type){

    return type.toString().toLowerCase();

  }

  static Future<void> createTable(Type tableT) async {

    String tname = getTable(tableT);

    if (tables.indexOf("," + tname + ",") == -1) {
      StringBuffer sql = StringBuffer("CREATE TABLE IF NOT EXISTS  $tname");
      sql.write(" (keyid varchar PRIMARY KEY");

      int fieldcount = Class.getFieldCount(tableT);

      for (int i = 0; i < fieldcount; i++) {
        Type f_type = Class.getFieldT(tableT, i);
        String f_name = Class.getFieldName(tableT, i);

        if (f_name == keyid) {
          continue;
        } else if (f_name.endsWith("_")) {
          continue;
        }
        sql.write(", $f_name");

        if (f_type == int) {
          sql.write(" int ");
        } else if (f_type == String) {
          sql.write(" varchar ");
        } else if (f_type == num) {
          sql.write(" float ");
        } else if (f_type == double) {
          sql.write(" numeric(10,5) ");
        } else if (f_type == bool) {
          sql.write(" bit ");
        }
      }
      sql.write(")");
      await database.execute(sql.toString(), null);
      return initTables();
    }
    return Future<void>.value();

  }

  static Future<int> insert(Object value) async {

    Type tableT = value.runtimeType;

    await createTable(value.runtimeType);

    Map<String,dynamic> params = Map();

    params[keyid] = sequence();

    int fieldcount = Class.getFieldCount(tableT);

    for (int i = 0; i < fieldcount; i++) {
      Type f_type = Class.getFieldT(tableT, i);
      String f_name = Class.getFieldName(tableT, i);

      if (f_name == keyid) {
        params[f_name] = sequence();
        continue;
      } else if (f_name.endsWith("_")) {
        continue;
      }
      Object f_value = Class.getValue(value, f_name);
      if (f_value == null) continue;
      if (f_type == bool) {
        params[f_name] = (f_value as bool) ? 1 : 0;
      } else
        params[f_name] = f_value;
    }
    return database.insert(tableT.toString(), params);
  }


  static String assembleSql(Type type,[String sqlWhere]){

    String tname = getTable(type);

    StringBuffer sql = StringBuffer();
    if (sqlWhere != null) {
      String where = sqlWhere.trim().toLowerCase();
      if (where.length > 0) {
        if (where.startsWith("select")) {
          sql.write(sqlWhere);
        } else {

          if (where.startsWith("where")) {
            sql.write("select * from $tname $where");
          }
          else
            sql.write("select * from $tname where $where");


        }
      }

    }
    if( sql.length == 0 )
      sql.write("select * from $tname");

    return sql.toString();

  }

  static Future<List> list(Type type, [String sqlWhere]) async {

    String sql = assembleSql(type,sqlWhere);

    print("list sql:$sql");

    List result = List();

    List<Map<String,dynamic>> rows = await database.rawQuery(sql.toString());
    for(int i=0;i<rows.length;i++){

       Object rowobj =  Class.fromJson(type, rows[i]);
       result.add(rowobj);

    }

    return Future.value(result);;


  }

  static Future<Object> queryOne(Type type, [String sqlWhere]) async{

    String sql = assembleSql(type,sqlWhere);

    print("list sql:$sql");

    List<Map<String,dynamic>> rows = await database.rawQuery(sql.toString());
    for(int i=0;i<rows.length;i++){
      //print("row ${rows[i].runtimeType}");
      Object rowobj =  Class.fromJson(type, rows[i]);
      return Future.value(rowobj);

    }

    return Future.value(null);

  }

  static Future<void> exec(String sql) async {

    return database.execute(sql);

  }

  static Future<void> delete(Object value) async {


    await createTable(value.runtimeType);

    String tname = getTable(value.runtimeType);


    String key = Class.getValue(value,keyid) as String;


    String sql = "delete from $tname where keyid='$key'";

    return exec(sql);

  }

  static Future<void> drop(Type type) async {

    String tname = getTable(type);
    String sql = "DROP TABLE IF EXISTS $tname";
    await exec(sql);

    return initTables();

  }

  static Future<int> updaate(Object value) async {

    Type tableT = value.runtimeType;

    await createTable( tableT );
    String tname = getTable( tableT );

    String key = Class.getValue(value, keyid) as String;

    String wherestr = "$keyid='$key'";



    Map<String,dynamic> params = Map();

    params[keyid] = sequence();

    int fieldcount = Class.getFieldCount(tableT);

    for (int i = 0; i < fieldcount; i++) {
      Type f_type = Class.getFieldT(tableT, i);
      String f_name = Class.getFieldName(tableT, i);

      if (f_name == keyid) {
        continue;
      } else if (f_name.endsWith("_")) {
        continue;
      }
      Object f_value = Class.getValue(value, f_name);
      if (f_value == null) {

        if( f_type == bool)
          params[f_name] = 0;
        else
          params[f_name] = null;

      }
      else if (f_type == bool) {
        params[f_name] = (f_value as bool) ? 1 : 0;
      } else
        params[f_name] = f_value;
    }


    return database.update(tname, params, where:wherestr);

  }


}
