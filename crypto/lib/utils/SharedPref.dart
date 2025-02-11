import 'package:shared_preferences/shared_preferences.dart';

class SharedPreferencesManager {
  static final SharedPreferencesManager instance = SharedPreferencesManager._internal();
  late SharedPreferences _prefs;

  factory SharedPreferencesManager() {
    return instance;
  }

  SharedPreferencesManager._internal();
  

  Future<void> init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  void saveString(String key, String value) {
    _prefs.setString(key, value);
  }

  String? getString(String key) {
    return _prefs.getString(key);
  }
  
}
