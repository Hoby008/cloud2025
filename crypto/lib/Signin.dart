// ignore_for_file: unused_local_variable, use_build_context_synchronously 
import 'dart:convert';
import 'package:crypto/types/User.dart';
import 'package:crypto/utils/SharedPref.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

class Signin extends StatefulWidget {
  const Signin({super.key});

  @override
  State<Signin> createState() => _SigninState();
}

class _SigninState extends State<Signin> {
  TextEditingController emailController = TextEditingController();
  TextEditingController mdpController = TextEditingController();
  String errorMessage = '';
  
  

Future<void> loginUser() async {
  const String apiUrl = 'http://192.168.232.172:8081/api/auth/login';

  try {
    var response = await http.post(
      Uri.parse(apiUrl),
      headers: {
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'email': emailController.text,
        'password': mdpController.text,
      }),
    );
    print('Réponse brute de l\'API: ${response.body}');
    print('Code HTTP: ${response.statusCode}');

    if (response.statusCode == 200) {
      final Map<String, dynamic> responseData = json.decode(response.body);

      print('Réponse JSON: $responseData');

      if (responseData['id'] != null && responseData['username'] != null && responseData['email'] != null) {
        int userId = responseData['id']; // 'id' est traité comme un int
        String username = responseData['username'].toString(); // Convertir 'username' en String
        String email = responseData['email'].toString(); // Convertir 'username' en String
      print('ID reçu: ${responseData['id']}');
      print('Username reçu: ${responseData['username']}');
      print('email reçu: ${responseData['email']}');
        if (username.isEmpty && email.isEmpty) {
          setState(() {
            errorMessage = "Les données utilisateur sont incomplètes.";
          });
          print('Données utilisateur manquantes ou invalides');
          return;
        }

        // Créer un utilisateur avec les données récupérées
        final userConnected = User.fromJson({
          'id': userId,
          'username': username,
          'email': email,
          'password': '', // Ne pas stocker le mot de passe
        });
        await SharedPreferencesManager.instance.init();
        // Sauvegarder l'utilisateur dans SharedPreferences
        SharedPreferences prefs = await SharedPreferences.getInstance();
        await prefs.setString("userConnected", jsonEncode(userConnected.toJson()));

        // Rediriger l'utilisateur vers la page d'accueil
        GoRouter.of(context).go("/dashboard/Acceuil");
      } else {
        // Si la réponse ne contient pas les données nécessaires
        setState(() {
          errorMessage = "Données utilisateur manquantes.";
        });
        print('Réponse de l\'API ne contient pas les données attendues.');
      }
    } else {
      // Afficher le code HTTP et la réponse en cas d'erreur
      setState(() {
        errorMessage = 'Erreur de connexion : ${response.statusCode}';
      });
      print('Erreur HTTP: ${response.statusCode}, Réponse: ${response.body}');
    }
  } catch (e) {
    // Capturer les exceptions et afficher plus de détails
    setState(() {
      errorMessage = 'Impossible de se connecter au serveur.';
    });
    print('Erreur de connexion : $e');
  }
}


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      body: Center(
              child: SingleChildScrollView(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    const Padding(
                      padding: EdgeInsets.all(8.0),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Icon(
                            Icons.money_sharp,
                            color: Colors.white,
                            size: 40,
                          ),
                          SizedBox(width: 8),
                          Text(
                            'Crypto',
                            style: TextStyle(
                              color: Colors.white,
                              fontSize: 40,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ],
                      ),
                    ),
                    Center(
                      child: Container(
                        constraints: const BoxConstraints(maxWidth: 400),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          border: Border.all(
                            color: const Color.fromARGB(255, 74, 70, 70),
                            width: 2.1,
                          ),
                          borderRadius: BorderRadius.circular(30.0),
                        ),
                        padding: const EdgeInsets.all(30),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.stretch,
                          children: [
                            const Text(
                              "Connectez-vous",
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                fontSize: 30,
                                fontWeight: FontWeight.bold,
                                color: Colors.black,
                              ),
                            ),
                            const SizedBox(height: 30),
                            TextField(
                              controller: emailController,
                              keyboardType: TextInputType.emailAddress,
                              decoration: const InputDecoration(
                                labelText: 'Votre email',
                                border: OutlineInputBorder(),
                              ),
                            ),
                            const SizedBox(height: 30),
                            TextField(
                              controller: mdpController,
                              obscureText: true,
                              decoration: const InputDecoration(
                                labelText: 'Votre mot de passe',
                                border: OutlineInputBorder(),
                              ),
                            ),
                            const SizedBox(height: 20),
                            if (errorMessage.isNotEmpty)
                              Text(
                                errorMessage,
                                style: const TextStyle(color: Colors.red),
                              ),
                            ElevatedButton(
                              onPressed: loginUser,
                              style: ButtonStyle(
                                backgroundColor: MaterialStateProperty.all<Color>(
                                    const Color.fromARGB(255, 210, 90, 90)),
                              ),
                              child: const Text(
                                'Se connecter',
                                style: TextStyle(
                                  color: Color.fromARGB(255, 240, 237, 237),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            
          );
  }
}
