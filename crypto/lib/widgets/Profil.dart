import 'dart:convert';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;

class ProfilPage extends StatefulWidget {
  const ProfilPage({super.key});

  @override
  State<ProfilPage> createState() => _ProfilPageState();
}

class _ProfilPageState extends State<ProfilPage> {
  int? userId;
  String? username;
  String? email;
  File? _image;
  final ImagePicker picker = ImagePicker();
  bool _isUploading = false;

  @override
void initState() {
  super.initState();
  checkStorageAccess();
  _requestPermission();
  _loadUserData();
  _loadProfileImage();
}

  Future<void> _loadUserData() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? userData = prefs.getString("userConnected");

    if (userData != null) {
      Map<String, dynamic> userMap = jsonDecode(userData);
      setState(() {
        userId = userMap['id'];
        username = userMap['username'];
        email = userMap['email'] ?? "Email non disponible";
      });
    }
  }

  Future<void> _requestPermission() async {
  var status = await Permission.photos.request();
  if (status.isDenied) {
    _showMessage("Permission refusée pour accéder aux images");
  }
}

Future<void> _pickImage() async {
  try {
    final pickedFile = await picker.pickImage(source: ImageSource.gallery);

    if (pickedFile != null) {
      File imageFile = File(pickedFile.path);

      // 🔹 Vérification du fichier
      if (await imageFile.exists()) {
        print("✅ Image trouvée : ${imageFile.path}");

        setState(() {
          _image = imageFile;
        });

        await _saveProfileImage(pickedFile.path);

        if (userId != null) {
          await _uploadProfileImage(userId!, _image!);
        }
      } else {
        print("❌ Le fichier image sélectionné n'existe pas.");
        _showMessage("Erreur : Le fichier sélectionné n'existe pas.");
      }
    }
  } catch (e) {
    print("🚨 Erreur lors de la sélection de l'image : $e");
    _showMessage("Erreur lors de la sélection de l'image : $e");
  }
}

  Future<void> _saveProfileImage(String imagePath) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setString('profile_image', imagePath);
  }

  Future<void> _loadProfileImage() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? imagePath = prefs.getString('profile_image');
    if (imagePath != null && File(imagePath).existsSync()) {
      setState(() {
        _image = File(imagePath);
      });
    }
  }

Future<void> _uploadProfileImage(int userId, File imageFile) async {
  const String apiUrl = 'http://192.168.232.172:8081/api/auth/modifier-image';

  setState(() {
    _isUploading = true;
  });

  try {
    var request = http.MultipartRequest('PATCH', Uri.parse('$apiUrl/$userId'))
      ..headers.addAll({
        "Content-Type": "multipart/form-data",
      })
      ..files.add(await http.MultipartFile.fromPath(
        'image', imageFile.path,
        filename: "profile_${userId}.jpg",
      ));

    print("📤 Envoi de l'image : ${imageFile.path}");
    print("📝 Headers : ${request.headers}");
    print("📝 Méthode HTTP : ${request.method}");

    var response = await request.send();
    var responseBody = await response.stream.bytesToString();

    print("🖥️ Réponse serveur : ${response.statusCode} - $responseBody");

    setState(() {
      _isUploading = false;
    });

    if (response.statusCode == 200) {
      _showMessage("✅ Image mise à jour avec succès !");
    } else {
      _showMessage("⚠️ Erreur serveur : ${response.statusCode} - $responseBody");
    }
  } catch (e) {
    setState(() {
      _isUploading = false;
    });
    print("🚨 Erreur lors de l'upload : $e");
    _showMessage("❌ Erreur lors de l'upload : $e");
  }
}
Future<void> checkStorageAccess() async {
  final directory = Directory('/storage/emulated/0/Download');
  if (await directory.exists()) {
    _showMessage("Accès au stockage OK !");
  } else {
    _showMessage("Problème d'accès au stockage !");
  }
}

  void _showMessage(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message)),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Profil"),
        backgroundColor: Colors.black,
      ),
      body: Center(
        child: userId == null
            ? const CircularProgressIndicator()
            : Card(
                margin: const EdgeInsets.all(20),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(15),
                ),
                elevation: 5,
                child: Padding(
                  padding: const EdgeInsets.all(20),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      GestureDetector(
                        onTap: _pickImage,
                        child: Stack(
                          children: [
                            CircleAvatar(
                              radius: 50,
                              backgroundImage: _image != null
                                  ? FileImage(_image!)
                                  : const AssetImage("assets/image/test.jpg") as ImageProvider,
                              backgroundColor: Colors.grey[300],
                            ),
                            
                            if (_isUploading)
                              const Positioned.fill(
                                child: Center(
                                  child: CircularProgressIndicator(color: Colors.white),
                                ),
                              ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 10),
                      ElevatedButton(
                        onPressed: _pickImage,
                        child: const Text("Changer l'image"),
                      ),
                      const SizedBox(height: 10),
                      Text(
                        "ID: $userId",
                        style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 10),
                      Text(
                        "Nom d'utilisateur: $username",
                        style: const TextStyle(fontSize: 18),
                      ),
                      const SizedBox(height: 10),
                      Text(
                        "Email: $email",
                        style: const TextStyle(fontSize: 18, color: Colors.grey),
                      ),
                    ],
                  ),
                ),
              ),
      ),
    );
  }
}
