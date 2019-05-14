import 'jsr/Class.dart';

const Object jsr = _jsr();

class _jsr {
  const _jsr();
}

class JsonR {

  String keyid;

  Map<String, dynamic> toJson() {
    //print("toJson table:$runtimeType");
    Map<String, dynamic> result = Map();
    for (int i = 0; i < Class.getFieldCount(runtimeType); i++) {
      String fieldname = Class.getFieldName(runtimeType, i);
      result[fieldname] = Class.getValue(this, fieldname);
    }
    return result;
  }
}
