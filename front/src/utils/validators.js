// Validation des formulaires
export const validators = {
  required: (value) => !!value || 'Ce champ est requis',
  
  email: (value) => {
    const pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    return pattern.test(value) || 'Adresse email invalide'
  },
  
  minLength: (length) => (value) => 
    (value && value.length >= length) || `Minimum ${length} caractères requis`,
  
  maxLength: (length) => (value) => 
    (value && value.length <= length) || `Maximum ${length} caractères autorisés`,
  
  passwordStrength: (value) => {
    const hasUpperCase = /[A-Z]/.test(value)
    const hasLowerCase = /[a-z]/.test(value)
    const hasNumbers = /[0-9]/.test(value)
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(value)
    
    return (
      (hasUpperCase && hasLowerCase && hasNumbers && hasSpecialChar && value.length >= 8) ||
      'Mot de passe trop faible. Utilisez majuscules, minuscules, chiffres et caractères spéciaux'
  )},
  
  // Validation spécifique montant
  amount: (value) => {
    const numValue = parseFloat(value)
    return (
      (!isNaN(numValue) && numValue > 0) || 
      'Montant invalide. Doit être un nombre positif'
  )},
  
  // Validation crypto
  cryptoAmount: (value) => {
    const numValue = parseFloat(value)
    return (
      (!isNaN(numValue) && numValue > 0 && numValue <= 21000000) || 
      'Montant de crypto invalide'
  )}
}

// Fonction de validation composite
export function validateForm(rules, values) {
  const errors = {}
  
  Object.keys(rules).forEach(field => {
    const fieldRules = rules[field]
    const value = values[field]
    
    for (const rule of fieldRules) {
      const result = rule(value)
      if (result !== true) {
        errors[field] = result
        break
      }
    }
  })
  
  return {
    isValid: Object.keys(errors).length === 0,
    errors
  }
}
