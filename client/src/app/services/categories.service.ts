import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})

export class CategoriesService {

    categories = [
        {
            nom: 'Meubles',
            value: 'meubles',
            description: 'Meubles de décoration, tout le mobilier design pour la maison.'
        },
        {
            nom: 'electroménager',
            value: 'electromenager',
            description: 'Tous les produits du quotidien sont dans notre univers électroménager.'
        },
        {
            nom: 'maison & jardin',
            value: 'maison-jardin',
            description: 'Découvrez un choix infini de meubles bon marché pour améliorer votre maison et jardin.'
        },
        {
            nom: 'culture',
            value: 'culture',
            description: 'Des livres, de la musique, du piano à la guitare.'
        },
        {
            nom: 'vêtements',
            value: 'vetements',
            description: 'Découvrez de belles marques de créateurs vêtements et des accessoires d\'occasion.'
        },
        {
            nom: 'bricolage',
            value: 'bricolage',
            description: 'Tout pour rénover, aménager votre maison à prix réduit.'
        },
        {
            nom: 'sport & loisir',
            value: 'sport-loisir',
            description: 'Équipement sportif neuf et d\'occasion pour tous vos loisirs et sports.'
        },
        {
            nom: 'technologie',
            value: 'technologie',
            description: 'Toute la téléphonie et l’informatique de seconde main.'
        },
        {
            nom: 'autres',
            value: 'autres',
            description: ' Produits uniques et rares que vous avez toujours désiré à bas prix.'
        },
    ];

    constructor() { }

    get() {
        return this.categories;
    }

    getByValue(categoryValue) {
        const category = this.categories.find(cat => cat.value === categoryValue);
        return category;
    }
}
