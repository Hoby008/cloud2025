import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:crypto/widgets/Transaction.dart'; // Make sure you import the correct file

class HistoriqueTransaction extends StatefulWidget {
  final int userId; // Pass the user ID from the previous screen

  HistoriqueTransaction({required this.userId});

  @override
  _HistoriqueTransactionPageState createState() =>
      _HistoriqueTransactionPageState();
}

class _HistoriqueTransactionPageState extends State<HistoriqueTransaction> {
  late Future<List<Transaction>> transactions;

  // Fetch transactions from the API
  Future<List<Transaction>> fetchTransactions() async {
    final response = await http.get(
      Uri.parse('http://192.168.232.172:8081/api/crypto/historique'),
    );

    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      return data.map((item) => Transaction.fromJson(item)).toList();
    } else {
      throw Exception('Failed to load transactions');
    }
  }

  @override
  void initState() {
    super.initState();
    transactions = fetchTransactions();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Historique Transactions')),
      body: FutureBuilder<List<Transaction>>(
        future: transactions,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else {
            final transactions = snapshot.data!;
            return ListView.builder(
              itemCount: transactions.length,
              itemBuilder: (context, index) {
                final transaction = transactions[index];
                return ListTile(
                  title: Text('Type: ${transaction.type}'),
                  subtitle: Text('Amount: ${transaction.montant} - Price: ${transaction.prix}'),
                );
              },
            );
          }
        },
      ),
    );
  }
}
