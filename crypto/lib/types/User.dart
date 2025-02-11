class User {
  final String username;
  final String password;
  final String email;
  final int userid;

  User({
    required this.password,
    required this.email,
    required this.username,required this.userid,
  });

  factory User.fromJson(Map<String, dynamic> json) {
  return User(
    password: json['password'] ?? "", // Éviter null
    username: json['username'] ?? "Inconnu", // Éviter null
    email: json['email'] ?? "Inconnu", // Éviter null
    userid: json['id'] ?? 0, // Éviter null
  );
}

  Map<String, dynamic> toJson() {
  return {
    'id': userid,  
    'username': username,   
    'password': password,
    'email': email,
  };
}
}
