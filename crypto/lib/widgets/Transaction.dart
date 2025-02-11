import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class Transaction extends StatefulWidget {
  
  final int userId;
  final String type;   // Type of transaction (e.g., "achat", "vente", etc.)
  final double montant; // The amount of cryptocurrency
  final double prix;    // The price of the cryptocurrency at the time of transaction

  Transaction({
    required this.userId,
    required this.type,
    required this.montant,
    required this.prix,
  });

  // Factory method to create a Transaction instance from JSON
  factory Transaction.fromJson(Map<String, dynamic> json) {
    return Transaction(
      userId: json['id'],
      type: json['type'] ?? 'Unknown', // Ensure 'type' is a string, default if missing
      montant: (json['montant'] ?? 0.0).toDouble(), // Ensure montant is parsed as double
      prix: (json['prix'] ?? 0.0).toDouble(), // Ensure prix is parsed as double
    );
  }
  @override
  _TransactionPageState createState() => _TransactionPageState();
}

class _TransactionPageState extends State<Transaction> {
  final _typeController = TextEditingController();
  final _cryptoIdController = TextEditingController();
  final _montantController = TextEditingController();
  final _prixController = TextEditingController();

  Future<void> createTransaction() async {
    final response = await http.post(
      Uri.parse('http://192.168.232.172:8081/api/crypto/transaction'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'type': _typeController.text,
        'cryptoId': int.parse(_cryptoIdController.text),
        'montant': double.parse(_montantController.text),
        'prix': double.parse(_prixController.text),
      }),
    );

    if (response.statusCode == 200) {
      // Successfully created transaction
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Transaction successful')));
    } else {
      // Handle error
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Failed to create transaction')));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Transaction')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: <Widget>[
            TextField(controller: _typeController, decoration: InputDecoration(labelText: 'Type')),
            TextField(controller: _cryptoIdController, decoration: InputDecoration(labelText: 'Crypto ID')),
            TextField(controller: _montantController, decoration: InputDecoration(labelText: 'Montant')),
            TextField(controller: _prixController, decoration: InputDecoration(labelText: 'Prix')),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: createTransaction,
              child: Text('Demander'),
            ),
          ],
        ),
      ),
    );
  }
}


