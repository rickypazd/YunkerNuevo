SELECT 
    array_to_json(array_agg(obfin.*)) AS json
FROM 
    (
        SELECT 
            to_json(ra.*) AS rep_auto,
            to_json(rav.*) AS rep_auto_version, 
            to_json(ramo.*) AS rep_auto_modelo, 
            to_json(rama.*) AS rep_auto_marca
        FROM
            rep_auto ra,
            rep_auto_version rav,
            rep_auto_modelo ramo,
            rep_auto_marca rama
        WHERE
            ra.clave = '3026708'
        AND ra.estado = 0
        AND ra.id_version = rav.id
        AND rav.id_rep_auto_modelo = ramo.id
        AND ramo.id_rep_auto_marca = rama.id
    ) obfin