import 'package:crypto/utils/SharedPref.dart';
import 'package:flutter/material.dart';
import 'package:crypto/router.dart' as custom_route;

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await SharedPreferencesManager.instance.init();
  
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      debugShowCheckedModeBanner: false,
      routerConfig: custom_route.Router.router,
    );
  }
}

