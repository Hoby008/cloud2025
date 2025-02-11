import 'package:crypto/types/User.dart';
import 'package:crypto/utils/SharedPref.dart';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class MainLayout extends StatefulWidget {
  final Widget child;
  const MainLayout({Key? key, required this.child}) : super(key: key);

  @override
  State<MainLayout> createState() => _MainLayoutState();
}

class _MainLayoutState extends State<MainLayout> {

 
  @override
  void initState() {
    super.initState();
  }
 
@override
Widget build(BuildContext context) {
  User user = User.fromJson(jsonDecode(SharedPreferencesManager.instance.getString("userConnected")!));
        return Scaffold(
          appBar: AppBar(
        backgroundColor: Colors.black,
        leading: Container(
          color: Colors.red,
          child: PopupMenuButton(
            color: const Color.fromRGBO(58, 57, 66, 0.941),
            offset: const Offset(0, 50),
            icon: const Icon(
              Icons.list,
              color: Colors.white,
            ),
            itemBuilder: (context) => [
              const PopupMenuItem(
                enabled: false,
                child: Text("  Accueil",
                    style: TextStyle(color: Color.fromARGB(255, 246, 1, 1))),
              ),
              PopupMenuItem(
                value: 1,
                onTap: () {
                  GoRouter.of(context).go("/dashboard/Acceuil");
                },
                child:const Row(
                  children: [
                    Icon(
                      Icons.home,
                      color: Colors.red,
                    ),
                    Text(
                      "  Tableau de bord",
                      style: TextStyle(
                          color: Color.fromARGB(255, 255, 255, 255)),
                    ),
                  ],
                ),
              ),
              PopupMenuItem(
                value: 1,
                onTap: () {
                  GoRouter.of(context).go("/dashboard/Profil");
                },
                child:const Row(
                  children: [
                    Icon(
                      Icons.person,
                      color: Colors.red,
                    ),
                    Text(
                      "  Profil utilisateur",
                      style: TextStyle(
                          color: Color.fromARGB(255, 255, 255, 255)),
                    ),
                  ],
                ),
              ),
              PopupMenuItem(
                  value: 2,
                  onTap: () {
                    GoRouter.of(context).go("/dashboard/Transaction");
                  },
                  child:const Row(
                    children: [
                      Icon(Icons.sim_card, color: Colors.red),
                      Text("  Demande depot et retrait",
                          style: TextStyle(
                              color: Color.fromARGB(255, 255, 255, 255))),
                    ],
                  ),
                ),
                PopupMenuItem(
                  value: 2,
                  onTap: () {
                    GoRouter.of(context).go("/dashboard/Portefeuille");
                  },
                  child:const Row(
                    children: [
                      Icon(Icons.gps_fixed, color: Colors.red),
                      Text(" Portefeuille",
                          style: TextStyle(
                              color: Color.fromARGB(255, 255, 255, 255))),
                    ],
                  ),
                ),
                PopupMenuItem(
                  value: 3,
                  onTap: () {
                    GoRouter.of(context).go("/dashboard/Historique");
                  },
                  child:const Row(
                    children: [
                      Icon(
                        Icons.directions_car,
                        color: Colors.red,
                      ),
                      Text("  Historique transactios",
                          style: TextStyle(
                              color: Color.fromARGB(255, 255, 255, 255))),
                    ],
                  ),
                ),
              ],
              onSelected: (value) {
                // Gérez la sélection de l'option ici
              },
            ),
          ),
        title: const Center(
          child: Text(
            "CRYPTO",
            style: TextStyle(
              color: Color.fromARGB(255, 246, 1, 1),
            ),
          ),
        ),
        actions: [
          PopupMenuButton(
            color: const Color.fromRGBO(58, 57, 66, 0.941),
            offset: const Offset(0, 50),
            child: Row(
              children: [
                const Icon(
                  Icons.person,
                  color: Colors.white,
                ),
                Text(
                    user.username,
                    style: const TextStyle(color: Color.fromARGB(255, 232, 27, 27)),
                ),

              ],
            ),
            itemBuilder: (context) => [
              PopupMenuItem(
                value: 3,
                onTap: () {
                  GoRouter.of(context).go("/signin");
                },
                child: const Row(
                  children: [
                    Icon(
                      Icons.logout,
                      color: Colors.red,
                    ),
                    Text(
                      "   Logout",
                      style: TextStyle(
                        color: Color.fromARGB(255, 255, 255, 255),
                      ),
                    ),
                  ],
                ),
              ),
            ],
            onSelected: (value) {},
          ),
        ],
      ),
          body: widget.child,
    );

  }
}
