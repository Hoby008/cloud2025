import 'package:crypto/Signin.dart';
import 'package:crypto/layout/MainLayout.dart';
import 'package:crypto/widgets/Acceuil.dart';
import 'package:crypto/widgets/Historique.dart';
import 'package:crypto/widgets/Portefeuille.dart';
import 'package:crypto/widgets/Profil.dart';
import 'package:crypto/widgets/Transaction.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';


class Router {
  static final router = GoRouter(
    initialLocation: '/signin',
    routes: [
           GoRoute(
          path: '/signin',
          builder: ((context, state) {
            return const Signin();
          })),
      ShellRoute(
          builder: (BuildContext context, GoRouterState state, Widget child) {
             
              return MainLayout(child: child);
          },
          routes: [
            GoRoute(
          path: '/dashboard/Acceuil',
          builder: ((context, state) {
            return const Acceuil();
          })),
          
          GoRoute(
          path: '/dashboard/Profil',
          builder: ((context, state) {
            return const ProfilPage();
          })),

          GoRoute(
            path: '/dashboard/Portefeuille',
            builder: (context, state) {
              final int userId = int.parse(state.pathParameters['userId']!);
              return Portefeuille(userId: userId);
            },
          ),

          GoRoute(
            path: '/dashboard/Transaction',
            builder: (context, state) {
              final Map<String, dynamic> data = state.extra as Map<String, dynamic>;
              return Transaction(
                userId: data['userId'],
                type: data['type'],
                montant: data['montant'],
                prix: data['prix'],
              );
            },
          ),

          GoRoute(
            path: '/dashboard/Historique',
            builder: (context, state) {
              final int userId = int.parse(state.pathParameters['userId']!);
              return HistoriqueTransaction(userId: userId);
            },
          ),



          ])
    ],
  );
}

