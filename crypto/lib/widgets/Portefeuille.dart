import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class Portefeuille extends StatefulWidget {
  final int userId; // Pass the user ID from the previous screen

  Portefeuille({required this.userId});

  @override
  _PortefeuillePageState createState() => _PortefeuillePageState();
}

class _PortefeuillePageState extends State<Portefeuille> {
  late Future<PortefeuilleData> portefeuille;

  Future<PortefeuilleData> fetchPortefeuille() async {
    final response = await http.get(
      Uri.parse('http://192.168.232.172:8081/api/crypto/portefeuille'),
    );

    if (response.statusCode == 200) {
      return PortefeuilleData.fromJson(json.decode(response.body));
    } else {
      throw Exception('Failed to load portfolio');
    }
  }

  @override
  void initState() {
    super.initState();
    portefeuille = fetchPortefeuille();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Portefeuille')),
      body: FutureBuilder<PortefeuilleData>(
        future: portefeuille,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else {
            final portefeuille = snapshot.data!;
            return ListView(
              children: [
                Text('Balance: ${portefeuille.balance}'),
                // Add more details if necessary
              ],
            );
          }
        },
      ),
    );
  }
}

// Create a class to represent the Portefeuille Data
class PortefeuilleData {
  final double balance;

  PortefeuilleData({required this.balance});

  factory PortefeuilleData.fromJson(Map<String, dynamic> json) {
    return PortefeuilleData(
      balance: json['balance'] ?? 0.0,  // Handle potential null values
    );
  }
}
