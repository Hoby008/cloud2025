import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';


class Acceuil extends StatefulWidget {
  const Acceuil({Key? key}) : super(key: key);
  @override
  State<Acceuil> createState() => _AcceuilState();
}

class _AcceuilState extends State<Acceuil> {

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 140.0),
              child: const Text(
                'Espace Utilisateur',
                style: TextStyle(
                  color: Color.fromARGB(255, 245, 1, 9),
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
                textAlign: TextAlign.start,
              ),
            ),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 140.0),
              child: const Text(
                'Tableau de bord',
                style: TextStyle(
                  color: Color.fromARGB(255, 223, 207, 207),
                  fontSize: 16,
                ),
                textAlign: TextAlign.start,
              ),
            ),
            Row(
              children: [
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 20.0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      IconButton(
                        onPressed: () async {
                          GoRouter.of(context).go("/dashboard/Acceuil");
                        },
                        icon: const Icon(Icons.home),
                      ),
                      const Text(
                        ' / Espace utilisateur / Tableau de bord',
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          color: Color.fromARGB(255, 32, 30, 30),
                          fontSize: 15,
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ],
      ),
    ),
    );
  }
}
